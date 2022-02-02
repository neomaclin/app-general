package com.group.quasi.domain.util

import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.security._

object RsaSign extends AsymKeys {

  def sign(privateKey: PrivateKey, plainText: Array[Byte]): Array[Byte] = {
    val signer = Signature.getInstance("SHA1withRSA")
    signer.initSign(privateKey)
    signer.update(plainText)
    signer.sign()

  }

  def verify(publicKey: PublicKey, signedCipherTest: Array[Byte], plainText: Array[Byte]): Boolean = {
    val signer = Signature.getInstance("SHA1withRSA")
    signer.initVerify(publicKey)
    signer.update(plainText)
    signer.verify(signedCipherTest)
  }

  // public key from an array of bytes (from .der file or other data source)
  override def publicKeyFromBytes(bytes: Array[Byte]): PublicKey = {
    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes))
  }

  override def privateKeyFromBytes(bytes: Array[Byte]): PrivateKey = {
    KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes))
  }

  /** Do this in scala: # private key openssl genrsa -out rsa4096_private.pem 4096 openssl pkcs8 -topk8 -inform PEM
    * -outform DER -in rsa4096_private.pem -out rsa4096_private.der -nocrypt # public key openssl rsa -in
    * rsa4096_private.pem -pubout openssl rsa -in rsa4096_private.pem -pubout -outform DER -out rsa4096_public.der Refs:
    * https://docs.oracle.com/javase/8/docs/api/java/security/KeyPairGenerator.html
    */
  def genSigningKeyPair: KeyPair = {

    // println("[genSigningKeyPair] Providers:\n")
    // http://alvinalexander.com/scala/converting-java-collections-to-scala-list-map-array
    // import scala.collection.JavaConversions._
    // java.security.Security.getProviders().foreach(provider=>println(provider.getServices().foreach(svc=>println(svc.getAlgorithm()))))

    // options: https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator

    val kpg = java.security.KeyPairGenerator.getInstance("RSA")
    kpg.initialize(4096)
    val kp: KeyPair = kpg.genKeyPair
    kp

  }

  val GENERATED_RSA_PUBLIC_SUFFIX = "_public.der"
  val GENERATED_RSA_PRIVATE_SUFFIX = "_private.der"

}
