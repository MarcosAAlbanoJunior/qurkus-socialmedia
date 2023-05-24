package io.github.malbano.quarkussocial.rest.dto;

import java.time.LocalDateTime;

import io.github.malbano.quarkussocial.domain.model.Post;
import lombok.Data;

@Data
public class PostResponse {

	private String text;
	private LocalDateTime dataHora;
	
	public static PostResponse fromEntity(Post post) {
		var response = new PostResponse();
		response.setText(post.getPostText());
		response.setDataHora(post.getDataHora());
		return response;
	}
}
