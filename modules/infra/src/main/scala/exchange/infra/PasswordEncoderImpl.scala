package exchange.infra

import cats.Applicative
import cats.implicits._
import exchange.domain.infra
import org.springframework.security.crypto.password

class PasswordEncoderImpl[F[_]: Applicative](encoder: password.PasswordEncoder) extends infra.PasswordEncoder[F] {
  override def encode(rawPassword: String): F[String] =
    encoder.encode(rawPassword).pure[F]

  override def matches(rawPassword: String, encoded: String): F[Boolean] =
    encoder.matches(rawPassword, encoded).pure[F]
}
