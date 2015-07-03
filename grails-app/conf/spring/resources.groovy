import java.time.Clock

beans = {
    clock(Clock) { bean ->
        bean.factoryMethod = "systemDefaultZone"
    }
}
