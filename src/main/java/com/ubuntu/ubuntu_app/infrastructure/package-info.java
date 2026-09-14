/**
 * Adaptadores por feature ({@code infrastructure.<feature>}) + web (controllers).
 *
 * <p>Reglas:
 * <ul>
 *   <li>Solo implementan puertos de {@code application}; pueden importar entidades JPA y DTOs.</li>
 *   <li>Prohibido importar clases de otro feature fuera de {@code shared}, salvo puertos del consumidor.</li>
 * </ul>
 */
package com.ubuntu.ubuntu_app.infrastructure;
