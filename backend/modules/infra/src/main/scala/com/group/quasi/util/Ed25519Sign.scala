package com.group.quasi.util

import java.security.{KeyFactory, PrivateKey, PublicKey, SecureRandom}

object Ed25519Sign extends AsymKeys{
  import org.bouncycastle.jce.provider.BouncyCastleProvider

  import java.security.Security

  Security.addProvider(new BouncyCastleProvider)
  private val factory =  KeyFactory.getInstance("Ed25519")
  override def publicKeyFromBytes(publicKeyBytes: Array[Byte]): PublicKey = {
    import org.bouncycastle.asn1.edec.EdECObjectIdentifiers
    import org.bouncycastle.asn1.x509.{AlgorithmIdentifier, SubjectPublicKeyInfo}

    import java.security.spec.X509EncodedKeySpec
    val pubKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519), publicKeyBytes)
    val x509KeySpec = new X509EncodedKeySpec(pubKeyInfo.getEncoded)
    factory.generatePublic(x509KeySpec)
  }

  override def privateKeyFromBytes(privateKeyBytes: Array[Byte]): PrivateKey = {
    import org.bouncycastle.asn1.DEROctetString
    import org.bouncycastle.asn1.edec.EdECObjectIdentifiers
    import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
    import org.bouncycastle.asn1.x509.AlgorithmIdentifier

    import java.security.spec.PKCS8EncodedKeySpec
    val privKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519), new DEROctetString(privateKeyBytes))
    val pkcs8KeySpec = new PKCS8EncodedKeySpec(privKeyInfo.getEncoded)
    factory.generatePrivate(pkcs8KeySpec)
  }

  import org.bouncycastle.crypto.AsymmetricCipherKeyPair
  import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
  import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
  import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
  import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters

  def genSigningKeyPair(): Unit = {
    val RANDOM = new SecureRandom()
    val keyPairGenerator = new Ed25519KeyPairGenerator
    keyPairGenerator.init(new Ed25519KeyGenerationParameters(RANDOM))
    val asymmetricCipherKeyPair: AsymmetricCipherKeyPair = keyPairGenerator.generateKeyPair
    val privateKey: Ed25519PrivateKeyParameters = asymmetricCipherKeyPair.getPrivate.asInstanceOf[Ed25519PrivateKeyParameters]
    val publicKey: Ed25519PublicKeyParameters = asymmetricCipherKeyPair.getPublic.asInstanceOf[Ed25519PublicKeyParameters]
    val privateKeyEncoded: Array[Byte] = privateKey.getEncoded
    val publicKeyEncoded: Array[Byte] = publicKey.getEncoded
    println("private key:" + bytesToString(privateKeyEncoded))
    println("public key:" + bytesToString(publicKeyEncoded))
  }

}
