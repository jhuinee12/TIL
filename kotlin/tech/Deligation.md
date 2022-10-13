# Deligation
> 최초작성 : 2022.03.29

### Deligation Pattern
> *[참고링크](https://en.wikipedia.org/wiki/Delegation_pattern)*

Deligation Pattern(위임 패턴)이란, 객체가 상송과 동일하게 코드 재사용을 할 수 있도록하는 객체지향 디자인 패턴이다.

* 예제소스 (kotlin)
```kt
class Rectangle (val width: Int, val height: Int) {
    fun area() = width * height 
}

class Window (val bounds: Rectangle) {
    // Deligation
    fun aread() = bounds.area()
}
```

코틀린에서는 Deligation에 대한 특별한 기능이 내장되어 있어 다음과 같이 작성할 수 있다.

```kt
interface ClosedShape {
    fun area() : Int
}

class Rectangle (val width: Int, val height: Int): ClosedShape {
    overriade fun area() = width * height 
}

class Window (private val bounds: ClosedShape): ClosedShape by bounds
```