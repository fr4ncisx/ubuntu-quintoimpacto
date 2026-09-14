/**
 * Casos de uso y puertos por feature ({@code application.<feature>}).
 *
 * <p>Reglas:
 * <ul>
 *   <li>Los controllers solo conocen puertos {@code port.in}; los services solo puertos {@code port.out}.</li>
 *   <li>Ninguna clase de {@code application} importa de {@code infrastructure}.</li>
 *   <li>Comunicación entre features solo vía puertos; jamás clases concretas de otro feature.</li>
 * </ul>
 */
package com.ubuntu.ubuntu_app.application;
