package ee.ciszewsj.secureapplication.controllers;

import ee.ciszewsj.secureapplication.data.LoginRequest;
import ee.ciszewsj.secureapplication.data.RegisterRequest;
import ee.ciszewsj.secureapplication.repository.entity.ActivateAccount;
import ee.ciszewsj.secureapplication.repository.entity.User;
import ee.ciszewsj.secureapplication.repository.repositories.ActivateAccRepository;
import ee.ciszewsj.secureapplication.repository.repositories.UserRepository;
import ee.ciszewsj.secureapplication.services.DateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ActivateAccRepository activateAccRepository;
	private final DateService dateService;

	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("login_request", new LoginRequest());
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	@GetMapping("/perform_logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

	@GetMapping("/register")
	public String getRegister(Model model) {
		model.addAttribute("registerRequest", new RegisterRequest());
		return "register";
	}

	@PostMapping("/register")
	public String postCreateNote(@Valid RegisterRequest registerRequest, Errors errors, Model model) throws InterruptedException {
		if (errors.hasErrors()) {
			return "register";
		}
		Thread.sleep(3000);

		if (userRepository.findFirstByUsernameIgnoreCase(registerRequest.getUsername()).isPresent()
				|| userRepository.findFirstByEmailIgnoreCase(registerRequest.getEmail()).isPresent()) {
			model.addAttribute("registerError", true);
			return "register";
		}
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setIsValid(false);
		user = userRepository.save(user);
		try {
			ActivateAccount activateAcc = new ActivateAccount();
			activateAcc.setUser(user);
			activateAcc.setValidTime(dateService.getValidDate());
			activateAcc = activateAccRepository.save(activateAcc);
			model.addAttribute("uuid", "/activate/" + activateAcc.getId());
		} catch (Exception e) {
			log.error(Arrays.toString(e.getStackTrace()));
			user.setIsValid(true);
			userRepository.save(user);
		}

		return "login";
	}

	@GetMapping("/activate/{id}")
	public String activateAccount(@PathVariable("id") String uid) {
		ActivateAccount activateAcc = activateAccRepository.findById(uid).orElse(null);
		if (activateAcc == null) {
			return "redirect:/register";
		}
		if (activateAcc.getValidTime().compareTo(new Date()) < 0) {
			return "redirect:/register";
		}
		User a = activateAcc.getUser();
		a.setIsValid(true);
		userRepository.save(a);
		activateAccRepository.delete(activateAcc);
		return "redirect:/login";
	}

	@GetMapping("reset-passwd")
	public String getResetPassword() {

	}
}
