package com.savvasdalkitsis.gameframe.feature.history.model

import java.util.*

class MomentList<M : Moment<M>> constructor(c: Collection<M>) : ArrayList<M>(c), Moment<MomentList<M>> {

    constructor(m: M) : this(listOf<M>(m))

    override fun replicateMoment() = MomentList(this.map { it.replicateMoment() })

    override fun isIdenticalTo(moment: MomentList<M>) = size == moment.size &&
            this.zip(moment).all {
                (thisItem, otherItem) -> thisItem.isIdenticalTo(otherItem)
            }
}
