package com.wafflestudio.babble.chat.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Long createChatRoom(CreateChatRoomDto dto) {
        Member member = memberRepository.getByUserId(dto.getAuthUserId());
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create(dto.getRoomName(), member, dto.getHashTag(), dto.getLocation()));
        chatterRepository.save(Chatter.create(chatRoom, member, dto.getNickname()));
        return chatRoom.getId();
    }
}
