package com.unab.FacturaEnLinea.request;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
        private String idUsuario;
        private String nombreUsuario;
	public JwtResponse(String jwttoken, String idUsuario, String nombreUsuario) {
		this.jwttoken = jwttoken;
                this.idUsuario = idUsuario;
                this.nombreUsuario = nombreUsuario; 
	}

	public String getToken() {
		return this.jwttoken;
	}

        public String getIdUsuario() {
            return idUsuario;
        }

    public String getnombreUsuario() {
        return nombreUsuario;
    }
        
        
        
}