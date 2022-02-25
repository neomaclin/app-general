package com.group.quasi.util

import java.security._
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}

object RSASign extends AsymKeys {


  // public key from an array of bytes (from .der file or other data source)
  override def publicKeyFromBytes(bytes: Array[Byte]): PublicKey = {
    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes))
  }

  override def privateKeyFromBytes(bytes: Array[Byte]): PrivateKey = {
    KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes))
  }

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


}
