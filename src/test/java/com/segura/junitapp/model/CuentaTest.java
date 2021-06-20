package com.segura.junitapp.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import com.segura.junitapp.exceptions.DinerhoInsuficienteException;

//Test pruebas unitarias

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// cambiando el ciclo de vida del test
class CuentaTest {
	Cuenta cuenta;

	@BeforeEach
	void testMetodoTestInit() throws Exception {
		this.cuenta = new Cuenta("Lopez", new BigDecimal("100.12345"));
		System.out.println("iniciando el metodo");
	}

	@AfterEach
	void tearDown() {
		System.out.println("Finalizando el metodo de prueba");
	}

	@BeforeAll
	static void beforeAll() {
		System.out.println("Inicializando la clase Test");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("Finalizando el test");
	}

	@Test
	@DisplayName("Probando nombre de la cuenta")
	void testName() throws Exception {

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
	@DisplayName("Verificando el saldo de la cuenta")
	void testSaldoCuenta() throws Exception {

		assertNotNull(cuenta.getSalario());
		// Valor esperado con el salario real
		assertEquals(100.12345, cuenta.getSalario().doubleValue());

		// Comprobacion que saldo sea mayor que 0
		assertFalse(cuenta.getSalario().compareTo(BigDecimal.ZERO) < 0);

		assertTrue(cuenta.getSalario().compareTo(BigDecimal.ZERO) > 0);
	}

	// Test driven development(TDD)
	@Test
	@DisplayName("Testeando referencias de las cuentas")
	void testReferenciaCuenta() throws Exception {
		cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

		// assertNotEquals(cuenta2, cuenta);
		assertEquals(cuenta2, cuenta);
	}

	@Test
	@DisplayName("Verificando debito entre cuentass")
	void testDebitoCuenta() throws Exception {
		cuenta = new Cuenta("Jose", new BigDecimal("7000.900"));

		cuenta.debito(new BigDecimal("1000"));
		assertNotNull(cuenta.getSalario());
		assertEquals(6000, cuenta.getSalario().intValue());
		assertEquals("6000.900", cuenta.getSalario().toPlainString());
	}

	@Test
	@DisplayName("Verificando credito entre cuentass")
	void testCreditoCuenta() throws Exception {
		cuenta = new Cuenta("Jose", new BigDecimal("7000.900"));

		cuenta.credito(new BigDecimal("1000"));
		assertNotNull(cuenta.getSalario());
		assertEquals(8000, cuenta.getSalario().intValue());
		assertEquals("8000.900", cuenta.getSalario().toPlainString());
	}

	@Test
	@DisplayName("Comprobando dinero de la cuenta")
	void testDineroInsuficienteException() throws Exception {
		cuenta = new Cuenta("Jose", new BigDecimal("7000.900"));

		Exception exception = assertThrows(DinerhoInsuficienteException.class,
				() -> {
					cuenta.debito(new BigDecimal("8500"));
				});

		String actual = exception.getMessage();
		String esperado = "Dinero insuficiente";

		assertEquals(esperado, actual);
	}

	@Test
	@DisplayName("Verificando transferencia entre cuentass")
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
	// @Disabled // Deshabilita el test
	@DisplayName("probando relaciones entre las cuentas y el banco con assertAll")
	void testRelacionBancoCuentas() throws Exception {

		// Assertions.fail();// Para forzar el error
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

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testSoloWindows() throws Exception {

	}

	@Test
	@EnabledOnOs({ OS.LINUX, OS.MAC })
	void testSoloLinuxMac() throws Exception {

	}

	@Test
	@DisabledOnOs(OS.WINDOWS)
	void testNotWindows() throws Exception {

	}

	@Test
	@EnabledOnJre(JRE.JAVA_8)
	void testSoloJDK8() throws Exception {

	}

	@Test
	@EnabledOnJre(JRE.JAVA_11)
	void testSoloJDK11() throws Exception {

	}

	@Test
	@DisabledOnJre(JRE.JAVA_11)
	void testNotJDK11() throws Exception {

	}

	@Test
	void testImprimirSystemProperties() throws Exception {
		Properties properties = System.getProperties();

		properties.forEach((k, v) -> System.out.println(k + ":" + v));
	}

	@Test
	@EnabledIfSystemProperty(named = "java.version", matches = ".*11.*")
	void testJavaVersion() throws Exception {

	}

	@Test
	@DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
	void testSolo64B() throws Exception {

	}

	@Test
	@EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
	void testNot64B() throws Exception {

	}

	@Test
	@EnabledIfSystemProperty(named = "user.name", matches = "PC")
	void testuserName() throws Exception {

	}

	@Test
	@EnabledIfSystemProperty(named = "ENV", matches = "dev")
	void testDev() throws Exception {

	}

	@Test
	void testImprimirVarEntorno() throws Exception {
		Map<String, String> getenv = System.getenv();
		getenv.forEach((k, v) -> System.out.println(k + " =" + v));
	}

	@Test
	@EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk1.8.0_192.*")
	void testJavaHome() throws Exception {

	}

	@Test
	@EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
	void testProcesadores() throws Exception {

	}

	@Test
	@EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
	void testEnv() throws Exception {

	}

	@Test
	@DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "pro")
	void testEnvPro() throws Exception {

	}

	@Test
	@DisplayName("Verificando el saldo de la cuenta, con assumingThat")
	void testSaldoCuentaDevAssumeThat() throws Exception {
		boolean esDev = "pro".equals(System.getProperty("ENV"));
		assumingThat(esDev, () -> {
			assertNotNull(cuenta.getSalario());
			// Valor esperado con el salario real
			assertEquals(100.12345, cuenta.getSalario().doubleValue());

			// Comprobacion que saldo sea mayor que 0
			assertFalse(cuenta.getSalario().compareTo(BigDecimal.ZERO) < 0);

			assertTrue(cuenta.getSalario().compareTo(BigDecimal.ZERO) > 0);
		});

	}

	@Test
	@DisplayName("Verificando el saldo de la cuenta, con ASSUMPTIONS")
	void testSaldoCuentaDev() throws Exception {
		boolean esDev = "dev".equals(System.getProperty("ENV"));
		assumeTrue(esDev);

		assertNotNull(cuenta.getSalario());
		// Valor esperado con el salario real
		assertEquals(100.12345, cuenta.getSalario().doubleValue());

		// Comprobacion que saldo sea mayor que 0
		assertFalse(cuenta.getSalario().compareTo(BigDecimal.ZERO) < 0);

		assertTrue(cuenta.getSalario().compareTo(BigDecimal.ZERO) > 0);
	}
}
