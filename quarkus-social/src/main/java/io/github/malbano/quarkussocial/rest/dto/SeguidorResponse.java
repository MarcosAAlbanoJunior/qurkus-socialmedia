package io.github.malbano.quarkussocial.rest.dto;

import io.github.malbano.quarkussocial.domain.model.Seguidor;
import lombok.Data;

@Data
public class SeguidorResponse {

	private Long id;
	private String name;
	
	public SeguidorResponse() {
	}

	public SeguidorResponse(Seguidor seguidor) {
		this(seguidor.getId(), seguidor.getSeguidor().getName());
	}

	public SeguidorResponse(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
