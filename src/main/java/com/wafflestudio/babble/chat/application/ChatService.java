package com.wafflestudio.babble.chat.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wafflestudio.babble.chat.application.dto.ChatDto;
import com.wafflestudio.babble.chat.application.dto.ChatRoomDetailDto;
import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;
import com.wafflestudio.babble.chat.application.dto.ChatsDto;
import com.wafflestudio.babble.chat.application.dto.ChatterDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatRoomDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatterDto;
import com.wafflestudio.babble.chat.application.dto.GetChatRoomDto;
import com.wafflestudio.babble.chat.application.dto.GetLatestChatsDto;
import com.wafflestudio.babble.chat.domain.chat.Chat;
import com.wafflestudio.babble.chat.domain.chat.ChatRepository;
import com.wafflestudio.babble.chat.domain.chatroom.ChatRoom;
import com.wafflestudio.babble.chat.domain.chatroom.ChatRoomRepository;
import com.wafflestudio.babble.chat.domain.chatter.Chatter;
import com.wafflestudio.babble.chat.domain.chatter.ChatterRepository;
import com.wafflestudio.babble.chat.domain.chatter.Nickname;
import com.wafflestudio.babble.common.exception.BadRequestException;
import com.wafflestudio.babble.common.exception.ForbiddenException;
import com.wafflestudio.babble.location.application.LocationService;
import com.wafflestudio.babble.location.domain.Location;
import com.wafflestudio.babble.member.domain.Member;
import com.wafflestudio.babble.member.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final LocationService locationService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatterRepository chatterRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ChatRoomDetailDto getChatRoom(GetChatRoomDto dto) {
        ChatRoom chatRoom = chatRoomRepository.get(dto.getRoomId());
        Location userLocation = dto.getLocation();
        locationService.validateClose(userLocation, chatRoom.getLocation());
        // TODO: 최근 N개만 보여주도록 수정?
        List<ChatDto> chats = chatRepository.findAllByRoom(chatRoom).stream()
            .sorted(ChatService::sortByCreatedAtAndIdDesc)
            .map(ChatDto::of)
            .collect(Collectors.toList());
        int chatterCount = chatterRepository.countByRoom(chatRoom);
        Optional<Chatter> chatter = chatterRepository.findByRoomIdAndUserId(chatRoom.getId(), dto.getAuthUserId());
        Long myChatterId = chatter.map(Chatter::getId).orElse(0L);
        return ChatRoomDetailDto.of(chatRoom, userLocation, chatterCount, ChatsDto.of(myChatterId, chats));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getNearbyRooms(Double latitude, Double longitude) {
        Location userLocation = new Location(latitude, longitude);
        // TODO: 거리가 가까운 방들만 필터링하기!
        return chatRoomRepository.findAll().stream()
            .map(room -> ChatRoomResponseDto.of(room, userLocation))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatsDto getLatestChats(GetLatestChatsDto dto) {
        ChatRoom room = chatRoomRepository.get(dto.getRoomId());
        locationService.validateClose(dto.getLocation(), room.getLocation());
        Optional<Chatter> chatter = chatterRepository.findByRoomIdAndUserId(room.getId(), dto.getAuthUserId());
        Long myChatterId = chatter.map(Chatter::getId).orElse(0L);
        List<ChatDto> chats = chatRepository.findAllByRoomAndIdGreaterThan(room, dto.getLatestChatId())
            .stream()
            .sorted(ChatService::sortByCreatedAtAndIdDesc)
            .map(ChatDto::of)
            .collect(Collectors.toList());
        return ChatsDto.of(myChatterId, chats);
    }

    public Long createChatRoom(CreateChatRoomDto dto) {
        Member member = memberRepository.getByUserId(dto.getAuthUserId());
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(dto.getRoomName(), member, dto.getHashTag(), dto.getLocation()));
        chatterRepository.save(Chatter.create(chatRoom, member, dto.getNickname()));
        return chatRoom.getId();
    }

    public ChatterDto createChatter(CreateChatterDto dto) {
        ChatRoom chatRoom = chatRoomRepository.get(dto.getRoomId());
        locationService.validateClose(dto.getLocation(), chatRoom.getLocation());
        Member member = memberRepository.getByUserId(dto.getAuthUserId());
        if (chatterRepository.existsByRoomAndMember(chatRoom, member)) {
            throw new BadRequestException("이미 참여 중인 채팅방입니다");
        }
        if (chatterRepository.existsByRoomAndNickname(chatRoom, new Nickname(dto.getNickname()))) {
            throw new BadRequestException("이미 사용 중인 닉네임입니다.");
        }
        Chatter chatter = chatterRepository.save(Chatter.create(chatRoom, member, dto.getNickname()));
        return ChatterDto.of(chatter);
    }

    public ChatDto createChat(CreateChatDto dto) {
        Member member = memberRepository.getByUserId(dto.getAuthUserId());
        ChatRoom chatRoom = chatRoomRepository.get(dto.getRoomId());
        locationService.validateClose(dto.getLocation(), chatRoom.getLocation());
        Chatter chatter = chatterRepository.findByRoomAndMember(chatRoom, member)
            .orElseThrow(() -> new ForbiddenException("아직 참여 중이지 않은 채팅방입니다."));
        if (!dto.isChild()) {
            Chat chat = chatRepository.save(Chat.create(chatRoom, chatter, dto.getContent()));
            return ChatDto.of(chat);
        }
        Chat parentChat = chatRepository.getByIdAndRoom(dto.getParentChatId(), chatRoom);
        Chat chat = chatRepository.save(Chat.createChild(chatRoom, chatter, dto.getContent(), parentChat));
        return ChatDto.of(chat);
    }

    private static int sortByCreatedAtAndIdDesc(Chat a, Chat b) {
        if (Objects.equals(b.getCreatedAt(), a.getCreatedAt())) {
            return (int) (b.getId() - a.getId());
        }
        return (int) (b.getCreatedAt() - a.getCreatedAt());
    }
}
