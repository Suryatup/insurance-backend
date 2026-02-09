package com.example.insurance.service;

import com.example.insurance.FirebaseConfig;
import com.example.insurance.model.UserEntity;
import com.example.insurance.repository.UserDetRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Service
@Component
public class UserLoginService implements UserDetailsService {

    private Firestore firebase;

    @Autowired
    private UserDetRepository userDetRepository;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userDet;
        try {
            userDet = getByUserName(userName);
        } catch (ExecutionException e) {
            throw new UsernameNotFoundException("User Not Found");
        } catch (Exception e) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return new User(userDet.getUserName(),userDet.getPassWord(), Collections.singleton(new SimpleGrantedAuthority(userDet.getRole())));
    }

    public  UserEntity getByUserName(String userName) throws ExecutionException, InterruptedException,Exception {
        UserEntity user = userDetRepository.findByUserName(userName); //document.toObject(UserEntity.class);
        System.out.println(user);
        return user;
    }
}
