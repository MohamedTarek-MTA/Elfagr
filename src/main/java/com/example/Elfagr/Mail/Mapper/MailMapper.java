package com.example.Elfagr.Mail.Mapper;

import com.example.Elfagr.Mail.DTO.MailDTO;
import org.springframework.stereotype.Component;

@Component
public class MailMapper {
    public static MailDTO toDTO(String email , String code){
        return new MailDTO(email,code);
    }
}
