package com.segura.junitapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
//@NoArgsConstructor
public class Banco {
	private String name;

	private List<Cuenta> cuentas;

	public Banco() {
		cuentas = new ArrayList<Cuenta>();
	}

	public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
		origen.debito(monto);
		destino.credito(monto);
	}

	public void addCuentas(Cuenta cuenta) {
		cuentas.add(cuenta);
		cuenta.setBanco(this);

	}
}
