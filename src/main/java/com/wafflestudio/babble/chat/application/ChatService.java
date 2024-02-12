package com.wafflestudio.babble.chat.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wafflestudio.babble.chat.application.dto.ChatDto;
import com.wafflestudio.babble.chat.application.dto.ChatRoomDetailDto;
import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatRoomDto;
import com.wafflestudio.babble.chat.application.dto.GetChatRoomDto;
import com.wafflestudio.babble.chat.domain.Chat;
import com.wafflestudio.babble.chat.domain.ChatRepository;
import com.wafflestudio.babble.chat.domain.ChatRoom;
import com.wafflestudio.babble.chat.domain.ChatRoomRepository;
import com.wafflestudio.babble.chat.domain.Chatter;
import com.wafflestudio.babble.chat.domain.ChatterRepository;
import com.wafflestudio.babble.common.exception.ForbiddenException;
import com.wafflestudio.babble.member.domain.Member;
import com.wafflestudio.babble.member.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatterRepository chatterRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ChatRoomDetailDto getChatRoom(GetChatRoomDto dto) {
        ChatRoom chatRoom = chatRoomRepository.get(dto.getRoomId());
        // TODO: 거리가 너무 먼 경우에 대한 ForbiddenException 예외 처리 추가
        boolean isChatter = chatterRepository.existsByRoomIdAndUserId(chatRoom.getId(), dto.getAuthUserId());
        // TODO: 최근 N개만 보여주도록 수정?
        List<ChatDto> chats = chatRepository.findAll().stream()
            .sorted(ChatService::sortByCreatedAtAndIdDesc)
            .map(chat -> ChatDto.of(chat, chat.getChatter()))
            .collect(Collectors.toList());
        return ChatRoomDetailDto.of(chatRoom, isChatter, chats);
    }

    private static int sortByCreatedAtAndIdDesc(Chat a, Chat b) {
        if (Objects.equals(b.getCreatedAt(), a.getCreatedAt())) {
            return (int) (b.getId() - a.getId());
        }
        return (int) (b.getCreatedAt() - a.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getNearbyRooms(Double latitude, Double longitude) {
        // TODO: 거리가 가까운 방들만 필터링하기!
        return chatRoomRepository.findAll().stream()
            .map(ChatRoomResponseDto::of)
            .collect(Collectors.toList());
    }

    public Long createChatRoom(CreateChatRoomDto dto) {
        Member member = memberRepository.getByUserId(dto.getAuthUserId());
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(dto.getRoomName(), member, dto.getHashTag(), dto.getLocation()));
        chatterRepository.save(Chatter.create(chatRoom, member, dto.getNickname()));
        return chatRoom.getId();
    }

    public Long createChat(CreateChatDto dto) {
        Member member = memberRepository.getByUserId(dto.getAuthUserId());
        ChatRoom chatRoom = chatRoomRepository.get(dto.getRoomId());
        // TODO: 거리가 너무 먼 경우에 대한 ForbiddenException 예외 처리 추가
        Chatter chatter = chatterRepository.findByRoomAndMember(chatRoom, member)
            .orElseThrow(() -> new ForbiddenException("아직 참여 중이지 않은 채팅방입니다."));
        Chat chat = chatRepository.save(Chat.create(chatRoom, chatter, dto.getContent()));
        return chat.getId();
    }
}
