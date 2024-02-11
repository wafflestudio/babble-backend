package com.wafflestudio.babble.chat.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatRoomDto;
import com.wafflestudio.babble.chat.domain.ChatRoom;
import com.wafflestudio.babble.chat.domain.ChatRoomRepository;
import com.wafflestudio.babble.chat.domain.Chatter;
import com.wafflestudio.babble.chat.domain.ChatterRepository;
import com.wafflestudio.babble.member.domain.Member;
import com.wafflestudio.babble.member.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatterRepository chatterRepository;
    private final MemberRepository memberRepository;

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
}
