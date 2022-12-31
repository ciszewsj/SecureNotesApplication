package ee.ciszewsj.secureapplication.controllers;

import ee.ciszewsj.secureapplication.data.CreateNoteRequest;
import ee.ciszewsj.secureapplication.repository.entity.Note;
import ee.ciszewsj.secureapplication.repository.entity.User;
import ee.ciszewsj.secureapplication.repository.repositories.NoteRepository;
import ee.ciszewsj.secureapplication.services.AESService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.Cipher;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class Views {
	private final NoteRepository noteRepository;
	private final AESService aesService;

	@GetMapping("/")
	public String getMain() {
		return "main";
	}

	@GetMapping("/create_note")
	public String getCreateNote(Model model) {
		model.addAttribute("createNoteRequest", new CreateNoteRequest());
		return "create_note";
	}

	@PostMapping("/create_note")
	public String postCreateNote(@AuthenticationPrincipal User user, @Valid CreateNoteRequest createNoteRequest, Errors errors) {
		if (errors.hasErrors()) {
			return "create_note";
		}
		Note note = new Note();
		note.setName(createNoteRequest.getName());
		if (createNoteRequest.getPassword().length() > 0) {
			try {
				String result = aesService.doAction(createNoteRequest.getPassword(), createNoteRequest.getNote(), Cipher.ENCRYPT_MODE);
				note.setNote(result);
			} catch (Exception e) {
				log.error(e.toString());
				return "create_note";
			}
			note.setIsEncrypted(false);
		} else {
			note.setNote(createNoteRequest.getNote());
			note.setIsEncrypted(false);
		}
		note.setUser(user);
		noteRepository.save(note);
		return "redirect:/";
	}
}
