package com.tsystems.toil

trait RefinedSupport:
  /** No macro/metaprogramming support for Scala 3, so this is for static refined values, and we leave it to the reviewer */
  def as[R](v: Any): R = v.asInstanceOf[R]

object RefinedSupport extends RefinedSupport
