package ee.ciszewsj.secureapplication.controllers;

import ee.ciszewsj.secureapplication.data.CreateNoteRequest;
import ee.ciszewsj.secureapplication.repository.entity.Note;
import ee.ciszewsj.secureapplication.repository.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class Views {
	private final NoteRepository noteRepository;

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
	public String postCreateNote(@Valid CreateNoteRequest createNoteRequest, Errors errors) {
		if (errors.hasErrors()) {
			return "create_note";
		}
		Note note = new Note();
		note.setName(createNoteRequest.getName());
		note.setNote(createNoteRequest.getNote());
		note.setIsEncrypted(false);
		return "redirect:/";
	}
}
