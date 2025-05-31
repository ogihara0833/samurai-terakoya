package com.example.samuraitravel.service;
//サービス
//データベースに会員情報を登録できるようにする
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Role;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.form.UserEditForm;
import com.example.samuraitravel.repository.RoleRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	public User create(SignupForm signupForm) {
		User user = new User();
		Role role = roleRepository.findByName("ROLE_GENERAL");
		//ROLE_GENERAL（一般会員）登録
		
		user.setName(signupForm.getName());
        user.setFurigana(signupForm.getFurigana());
        user.setPostalCode(signupForm.getPostalCode());
        user.setAddress(signupForm.getAddress());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        //パスワードをハッシュ化する,encode()
        user.setRole(role);
        user.setEnabled(false);//メール認証済みかどうかの判定に利用するenabledフィールド
        
        return userRepository.save(user);
	}
	
	@Transactional
	//会員の更新処理を行うupdate()メソッドを定義
	public void update(UserEditForm userEditForm) {
		User user = userRepository.getReferenceById(userEditForm.getId());
		
		 user.setName(userEditForm.getName());
	        user.setFurigana(userEditForm.getFurigana());
	        user.setPostalCode(userEditForm.getPostalCode());
	        user.setAddress(userEditForm.getAddress());
	        user.setPhoneNumber(userEditForm.getPhoneNumber());
	        user.setEmail(userEditForm.getEmail());      
	        
	        userRepository.save(user);
	}

	 // メールアドレスが登録済みかどうかをチェックする
    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);  
        return user != null;
    }   
    
 // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);//equals()メソッドを使って文字列同士を比較する
    } 
    
    //ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
    	//メール認証用のページ（https://ドメイン名/signup/verify?token=生成したトークン）
    	//において、認証に成功した際に実行するメソッド
    	user.setEnabled(true);
    	userRepository.save(user);
    }
    
    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
    	User currentUser = userRepository.getReferenceById(userEditForm.getId());
    	return !userEditForm.getEmail().equals(currentUser.getEmail());
    }
}
