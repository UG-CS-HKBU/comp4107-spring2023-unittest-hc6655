import org.junit.Assert.assertTrue
import org.junit.Test

class UnitTest {
    /*@Test
    fun test1() {
        heroes.clear()
        monarchHero = CaoCao()
        heroes.add(monarchHero)
        heroes.add(ZhangFei(MinisterRole()))
        assertTrue(monarchHero.name == "Cao Cao")
    }

    @Test
    fun test2() {
        if (heroes.size < 2)
            test1()

        assertTrue(heroes.size == 2)
    }*/

    @Test
    fun testCaoDodgeAttack() {
        monarchHero = CaoCao()
        for (i in 0..6)
            heroes.add(NoneMonarchFactory.createRandomHero())

        assertTrue(monarchHero.dodgeAttack())
    }

    @Test
    fun testBeingAttacked() {
        if (heroes.isEmpty())
            heroes.add(NoneMonarchFactory.createRandomHero())

        for (hero in heroes) {
            val spy = object : WarriorHero(MinisterRole()) {
                override val name = hero.name

                override fun beingAttacked() {
                    hero.beingAttacked()
                    assertTrue(hero.hp >= 0)
                }
            }

            for (i in 0 .. 2)
                spy.beingAttacked()
        }
    }
}

object FakeNonmonarchFactory: GameObjectFactory {
    var count: Int = 0
    var last: WeiHero? = null

    override fun getRandomRole(): Role {
        return MinisterRole()
    }

    override fun createRandomHero(): Hero {
        val hero = when(count++) {
            0 -> SimaYi(getRandomRole())
            1 -> XuChu(getRandomRole())
            else -> XiaHouyuan(getRandomRole())
        }

        if (count > 2)
            count = 0

        val cao = monarchHero as CaoCao
        if (last == null)
            cao.helper = hero
        else
            last!!.setNext(hero)

        last = hero
        return hero;
    }
}

object FakeMonarchFactory : GameObjectFactory {
    override fun createRandomHero(): Hero {
        return CaoCao()
    }

    override fun getRandomRole(): Role {
        return MonarchRole()
    }
}

class CaoCaoUnitTest {
    @Test
    fun testCaoDodgeAttack() {
        monarchHero = FakeMonarchFactory.createRandomHero() as MonarchHero
        heroes.add(monarchHero)
        monarchHero.setCommand(Abandon(monarchHero))
        for (i in 0..2) {
            var hero = FakeNonmonarchFactory.createRandomHero()
            hero.index = heroes.size;
            heroes.add(hero)
        }

        for (hero in heroes) {
            hero.beingAttacked()
            hero.templateMethod()
        }

        assert(monarchHero.dodgeAttack())

    }
}

class DummyRole: Role {
    override val roleTitle = "Dummy"
    override fun getEnemy(): String {
        return ""
    }

    @Test
    fun testDiscardCards() {
        val hero = ZhangFei(this)
        hero.discardCards()
    }
}