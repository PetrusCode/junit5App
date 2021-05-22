package com.segura.junitapp.model;

import java.math.BigDecimal;

import com.segura.junitapp.exceptions.DinerhoInsuficienteException;

import lombok.Data;

@Data
public class Cuenta {
	private String persona;
	private BigDecimal salario;
	private Banco banco;

	public Cuenta(String persona, BigDecimal saldo) {
		this.salario = saldo;
		this.persona = persona;
	}

	public void debito(BigDecimal monto) {
		BigDecimal nuevoSaldo = this.salario.subtract(monto);
		if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
			throw new DinerhoInsuficienteException("Dinero insuficiente");
		}

		this.salario = nuevoSaldo;
	}

	public void credito(BigDecimal monto) {
		this.salario = this.salario.add(monto);
	}
}
