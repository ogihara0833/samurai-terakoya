package com.example.samuraitravel.controller;
//ユーザーが /admin/users にアクセス
//AdminUserController がリクエストを受け取る
//userRepository.findAll() などのメソッドを呼び出し、データを取得
//結果をビュー (HTML や JSON) に渡す
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
	private final UserRepository userRepository;
	
	public AdminUserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,Model model) {
		Page<User> userPage;
		
		if (keyword != null && !keyword.isEmpty()) {
			//氏名かフリガナを含む（％）検索を実行
			userPage = userRepository.findByNameLikeOrFuriganaLike("%" + keyword +"%", "%" + keyword + "%", pageable);
		} else {
			userPage = userRepository.findAll(pageable);
			//キーワードがなければ全て（faindAll）のユーザーをページネーション付き（pageableに基づき）で表示
		}
		
		model.addAttribute("userPage", userPage);
		model.addAttribute("keyword", keyword);
		
		
		
		return "admin/users/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		User user = userRepository.getReferenceById(id);
		
		model.addAttribute("user", user);
		return "admin/users/show";
	}
}
