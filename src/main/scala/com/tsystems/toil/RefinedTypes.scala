package com.tsystems.toil

import eu.timepit.refined.*
import eu.timepit.refined.api.{ RefType, Refined, RefinedTypeOps }
import eu.timepit.refined.auto.*
import eu.timepit.refined.numeric.*
import eu.timepit.refined.collection.*
import eu.timepit.refined.numeric.Interval.*
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.numeric.*

object RefinedTypes:
  type UtmZone = Int Refined Closed[1, 60]
  // provides an "unsafeFrom()" for UtmZone:
  object UtmZone extends RefinedTypeOps[UtmZone, Int]
