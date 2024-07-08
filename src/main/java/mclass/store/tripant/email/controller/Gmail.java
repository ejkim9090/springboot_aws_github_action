package mclass.store.tripant.email.controller;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mclass.store.tripant.apikeys.KeysJaewon;

@Component
@RequiredArgsConstructor
public class Gmail extends Authenticator {
	
	private final KeysJaewon keysJaewon;
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(keysJaewon.getGmail(), keysJaewon.getGmailPwd());
	}
}
