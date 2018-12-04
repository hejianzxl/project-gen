package ${groupId}.dao

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.util.*

//@Aspect
//@Component
open class AspectDao {

    @Around("execution(* ${groupId}.dao.mapper.**.insert*(..))")
    @Throws(Throwable::class)
    open fun doAroundInsert(pjp: ProceedingJoinPoint): Any {
        if (pjp.args != null && pjp.args.isNotEmpty()) {
            processForInsert(pjp.args[0])
        }
        return pjp.proceed()
    }

    @Around("execution(* ${groupId}.dao.mapper.**.update*(..))")
    @Throws(Throwable::class)
    open fun doAroundUpdate(pjp: ProceedingJoinPoint): Any {
        if (pjp.args != null && pjp.args.isNotEmpty()) {
            processForUpdate(pjp.args[0])
        }
        return pjp.proceed()
    }

    private fun processForInsert(obj: Any?) {
        obj ?: return
        val f = ReflectionUtils.findField(obj.javaClass, "createTime") ?: return
        f.isAccessible = true
        ReflectionUtils.setField(f, obj, Date())
        val v = ReflectionUtils.findField(obj.javaClass, "version") ?: return
        v.isAccessible = true
        ReflectionUtils.setField(v, obj, 1)
    }

    private fun processForUpdate(obj: Any?) {
        obj ?: return
        val f = ReflectionUtils.findField(obj.javaClass, "updateTime") ?: return
        f.isAccessible = true
        ReflectionUtils.setField(f, obj, Date())
        val v = ReflectionUtils.findField(obj.javaClass, "version") ?: return
        v.isAccessible = true
        ReflectionUtils.setField(v, obj, (v.get(obj)?.let { it as Int } ?: 0) + 1)
    }
}
