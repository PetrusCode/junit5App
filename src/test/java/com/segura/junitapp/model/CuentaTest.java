package com.segura.junitapp.model;

import static org.junit.jupiter.api.Assertions.assertAll;
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
		Cuenta cuenta = new Cuenta("Lopez f", new BigDecimal("100.12345"));
		// cuenta.setPersona("Lopez");

		String esperado = "Lopez";
		String realidad = cuenta.getPersona();
		// Usamos expresiones lambdas para mejorar el rendimiento al crear los
		// mensajes personalizados de cada caso, de esta manera unicamente se
		// instanciara el mensaje cuando la prueba falle
		assertNotNull(realidad, () -> "La cuenta no puede ser nula");
		assertEquals(esperado, realidad,
				() -> "El nombre de la cuenta no es el que se esperaba");
		assertTrue(realidad.equals("Lopez"),
				() -> "Nombre cuenta esperado debe ser igual al real");
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

	@Test
	void testTransferirDineroCuenta() throws Exception {
		Cuenta cuenta1 = new Cuenta("Guzman", new BigDecimal("2500"));
		Cuenta cuenta2 = new Cuenta("Pepe", new BigDecimal("1500.2500"));

		Banco banco = new Banco();
		banco.setName("Banco del Estado");

		banco.transferir(cuenta1, cuenta2, new BigDecimal("500"));

		assertEquals("2000", cuenta1.getSalario().toPlainString());
		assertEquals("2000.2500", cuenta2.getSalario().toPlainString());

	}

	@Test
	void testRelacionBancoCuentas() throws Exception {
		Cuenta cuenta1 = new Cuenta("Guzman", new BigDecimal("2500"));
		Cuenta cuenta2 = new Cuenta("Pepe", new BigDecimal("1500.2500"));

		Banco banco = new Banco();
		banco.addCuentas(cuenta1);
		banco.addCuentas(cuenta2);

		banco.setName("Banco del Estado");

		banco.transferir(cuenta1, cuenta2, new BigDecimal("500"));
		assertAll(() -> {
			assertEquals("2000", cuenta1.getSalario().toPlainString(),
					() -> "El valor del salario de la cuenta1 no es el que se esperaba");
		}, () -> {
			assertEquals("2000.2500", cuenta2.getSalario().toPlainString(),
					() -> "El valor del salario de la cuenta2 no es el que se esperaba");
		}, () -> {
			assertEquals(2, banco.getCuentas().size(),
					() -> "El banco no tiene las cuentas esperadas");

		}, () -> {
			assertEquals("Banco del Estado", cuenta1.getBanco().getName());

		}, () -> {
			// Evaluamos si el usuario existe dentro de la lista de las cuentas
			// de
			// banco (relacion inversa entre clase banco y cuenta)

			assertEquals("Guzman",
					banco.getCuentas().stream()
							.filter(c -> c.getPersona().equals("Guzman"))
							.findFirst().get().getPersona());
		}, () -> {
			// Forma usando assrtTrue
			assertTrue(banco.getCuentas().stream()
					.filter(c -> c.getPersona().equals("Guzman")).findFirst()
					.isPresent());

		}, () -> {
			// Forma mas simple
			assertTrue(banco.getCuentas().stream()
					.anyMatch(c -> c.getPersona().equals("Guzman")));
		});

	}

}
