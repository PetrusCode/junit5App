package com.segura.junitapp.model;

import java.math.BigDecimal;

public class Cuenta {
	private String persona;
	private BigDecimal salario;

	public String getPersona() {
		return persona;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

}
