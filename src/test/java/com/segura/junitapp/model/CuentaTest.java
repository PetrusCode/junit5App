package com.segura.junitapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.segura.junitapp.exceptions.DinerhoInsuficienteException;

//Test pruebas unitarias
class CuentaTest {

	@Test
	void testName() throws Exception {
		Cuenta cuenta = new Cuenta("Lopez", new BigDecimal("100.12345"));
		// cuenta.setPersona("Lopez");

		String esperado = "Lopez";
		String realidad = cuenta.getPersona();
		assertNotNull(realidad);
		assertEquals(esperado, realidad);
		assertTrue(realidad.equals("Lopez"));
	}

	@Test
	void testSaldoCuenta() throws Exception {
		Cuenta cuenta = new Cuenta("Lopez", new BigDecimal("100.12345"));

		assertNotNull(cuenta.getSalario());
		// Valor esperado con el salario real
		assertEquals(100.12345, cuenta.getSalario().doubleValue());

		// Comprobacion que saldo sea mayor que 0
		assertFalse(cuenta.getSalario().compareTo(BigDecimal.ZERO) < 0);

		assertTrue(cuenta.getSalario().compareTo(BigDecimal.ZERO) > 0);
	}

	// Test driven development(TDD)
	@Test
	void testReferenciaCuenta() throws Exception {
		Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

		// assertNotEquals(cuenta2, cuenta);
		assertEquals(cuenta2, cuenta);
	}

	@Test
	void testDebitoCuenta() throws Exception {
		Cuenta cuenta = new Cuenta("Jose", new BigDecimal("7000.900"));

		cuenta.debito(new BigDecimal("1000"));
		assertNotNull(cuenta.getSalario());
		assertEquals(6000, cuenta.getSalario().intValue());
		assertEquals("6000.900", cuenta.getSalario().toPlainString());
	}

	@Test
	void testCreditoCuenta() throws Exception {
		Cuenta cuenta = new Cuenta("Jose", new BigDecimal("7000.900"));

		cuenta.credito(new BigDecimal("1000"));
		assertNotNull(cuenta.getSalario());
		assertEquals(8000, cuenta.getSalario().intValue());
		assertEquals("8000.900", cuenta.getSalario().toPlainString());
	}

	@Test
	void testDineroInsuficienteException() throws Exception {
		Cuenta cuenta = new Cuenta("Jose", new BigDecimal("7000.900"));

		Exception exception = assertThrows(DinerhoInsuficienteException.class,
				() -> {
					cuenta.debito(new BigDecimal("8500"));
				});

		String actual = exception.getMessage();
		String esperado = "Dinero insuficiente";

		assertEquals(esperado, actual);
	}
}
