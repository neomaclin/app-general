package exchange.domain.infra

trait PasswordEncoder[F[_]] {
  def encode(rawPassword: String): F[String]

  def matches(rawPassword: String, encoded: String): F[Boolean]
}
