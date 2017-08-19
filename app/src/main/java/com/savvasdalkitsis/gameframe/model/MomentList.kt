package com.savvasdalkitsis.gameframe.model

import rx.Observable
import java.util.*

class MomentList<M : Moment<M>> : ArrayList<M>, Moment<MomentList<M>> {

    constructor(c: Collection<M>) : super(c)

    constructor(m: M) : this(listOf<M>(m))

    override fun replicateMoment(): MomentList<M> {
        return MomentList(Observable.from(this)
                .map<M>({ it.replicateMoment() })
                .toList()
                .toBlocking()
                .first())
    }

    override fun isIdenticalTo(moment: MomentList<M>): Boolean {
        return size == moment.size &&
                this.zip(moment).all { (thisItem, otherItem) -> thisItem.isIdenticalTo(otherItem) }
    }
}
