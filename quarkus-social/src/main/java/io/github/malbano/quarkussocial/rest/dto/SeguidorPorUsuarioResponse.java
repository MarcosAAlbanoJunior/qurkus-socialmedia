package io.github.malbano.quarkussocial.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class SeguidorPorUsuarioResponse {
	
	private Integer quantidadeSeguidores;
	private List<SeguidorResponse> content;

}
