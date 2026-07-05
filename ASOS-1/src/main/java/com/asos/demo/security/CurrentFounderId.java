package com.asos.demo.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller method parameter annotation. Resolves to the Long founder id
 * stored in the HttpSession by AuthController on login/register, and
 * validated on every request by SessionAuthFilter.
 *
 * Usage:
 *   @GetMapping
 *   public ResponseEntity<?> list(@CurrentFounderId Long founderId) { ... }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentFounderId {
}
