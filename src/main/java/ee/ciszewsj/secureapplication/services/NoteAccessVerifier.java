package ee.ciszewsj.secureapplication.services;

import ee.ciszewsj.secureapplication.data.AddPlayerAccess;
import ee.ciszewsj.secureapplication.repository.entity.Note;
import ee.ciszewsj.secureapplication.repository.entity.User;
import ee.ciszewsj.secureapplication.repository.entity.UserWithAccess;
import ee.ciszewsj.secureapplication.repository.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteAccessVerifier {
	private final NoteRepository noteRepository;

	public boolean verifyHaveAccess(User user, Note note, Model model) {
		if (Objects.equals(user.getId(), note.getUser().getId())) {
			model.addAttribute("owner", true);
			model.addAttribute("id", note.getId());
			model.addAttribute("addedUsers", note.getUsersWithAccess());
			model.addAttribute("addPlayerAccess", new AddPlayerAccess());
			return true;
		}
		if (note.getIsPublic()) {
			return true;
		}
		return note.getUsersWithAccess().stream().map(UserWithAccess::getName).anyMatch(o -> Objects.equals(o.toUpperCase(Locale.ROOT), user.getUsername().toUpperCase(Locale.ROOT)));
	}

	public void addNotes(User user, Model model) {
		List<Note> notes = noteRepository.findAll();
		if (user != null) {
			List<Note> myNotes = notes.stream().filter(o ->
					Objects.equals(o.getUser().getId(), user.getId())).collect(Collectors.toList());
			model.addAttribute("my_notes", myNotes);


			List<Note> agNotes = notes.stream().filter(o ->
							o.getUsersWithAccess()
									.stream()
									.anyMatch(a -> Objects.equals(a.getName().toUpperCase(Locale.ROOT), user.getUsername().toUpperCase(Locale.ROOT))))
					.collect(Collectors.toList());
			model.addAttribute("access_granted", agNotes);
		}
		List<Note> publicNotes = notes.stream().filter(Note::getIsPublic).collect(Collectors.toList());
		model.addAttribute("public_notes", publicNotes);
	}
}
