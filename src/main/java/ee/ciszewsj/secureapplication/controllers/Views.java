package ee.ciszewsj.secureapplication.controllers;

import ee.ciszewsj.secureapplication.data.CreateNoteRequest;
import ee.ciszewsj.secureapplication.repository.entity.Note;
import ee.ciszewsj.secureapplication.repository.entity.User;
import ee.ciszewsj.secureapplication.repository.repositories.NoteRepository;
import ee.ciszewsj.secureapplication.services.AESService;
import ee.ciszewsj.secureapplication.services.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.Cipher;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class Views {
	private final NoteRepository noteRepository;
	private final AESService aesService;
	private final HtmlSanitizer htmlSanitizer;

	@GetMapping("/")
	public String getMain(Model model) {
		model.addAttribute("notes", noteRepository.findAll());
		return "main";
	}

	@GetMapping("/create_note")
	public String getCreateNote(Model model) {
		model.addAttribute("createNoteRequest", new CreateNoteRequest());
		return "create_note";
	}

	@GetMapping("/show_note/{note_id}")
	public String getShowNote(Model model, @PathVariable("note_id") Long noteId) {
		Note note = noteRepository.findById(noteId).orElse(null);
		if (note == null) {
			model.addAttribute("element_not_found", true);
			return "main";
		}
		model.addAttribute("note", note);
		return "show_note";
	}

	@PostMapping("/show_note/{note_id}")
	public String postShowNote(@PathVariable("note_id") Long noteId) {
		return "show_note";
	}


	@PostMapping("/create_note")
	public String postCreateNote(@AuthenticationPrincipal User user, @Valid CreateNoteRequest createNoteRequest, Errors errors) {
		if (errors.hasErrors()) {
			return "create_note";
		}
		Note note = new Note();
		note.setName(createNoteRequest.getName());
		String note_text = htmlSanitizer.sanitize(createNoteRequest.getNote());
		if (createNoteRequest.getPassword().length() > 0) {
			try {
				String result = aesService.doAction(createNoteRequest.getPassword(), note_text, Cipher.ENCRYPT_MODE);
				note.setNote(result);
			} catch (Exception e) {
				log.error(e.toString());
				return "create_note";
			}
			note.setIsEncrypted(true);
		} else {
			note.setNote(note_text);
			note.setIsEncrypted(false);
		}
		note.setUser(user);
		noteRepository.save(note);
		return "redirect:/";
	}
}
