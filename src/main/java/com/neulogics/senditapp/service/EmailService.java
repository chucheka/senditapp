//package com.neulogics.senditapp.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//
//public class EmailService {
//
//	private JavaMailSender javaMailSender;
//
//	
//	public EmailService(JavaMailSender javaMailSender) {
//		this.javaMailSender = javaMailSender;
//	}
//	
//	public void sendEmail(String recipientEmail,String subject,String message) {
//		
//		SimpleMailMessage msg = new SimpleMailMessage();
//		msg.setTo(recipientEmail);
//		msg.setSubject(subject);
//		msg.setText(message);
//		javaMailSender.send(msg);
//		
//	}
//	
//}
