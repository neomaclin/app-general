package com.group.quasi.util

import java.security._
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}

object DSASign extends AsymKeys {


  // public key from an array of bytes (from .der file or other data source)
  override def publicKeyFromBytes(bytes: Array[Byte]): PublicKey = {
    KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(bytes))
  }

  override def privateKeyFromBytes(bytes: Array[Byte]): PrivateKey = {
    KeyFactory.getInstance("DSA").generatePrivate(new PKCS8EncodedKeySpec(bytes))
  }



}
