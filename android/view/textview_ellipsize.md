# TextView에서 끝에 ... 넣기
> 최초작성 : 2022.04.19

텍스트뷰를 한줄로 표시하면서, 글자수가 넘어갈 경우 생략 표시를 하려면 maxLines와 ellipsize를 사용하면 된다.

* ellipsize = "end" : 뒷부분을 ...으로 표시
* ellipsize = "middle" : 중간 부분을 ...으로 표시
* ellipsize = "none" : ...없이 뒷부분을 잘라서 표시 (default)

* maxLines : 최대 TextView 줄

```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:ellipsize="end"
    android:maxLines="1"
    android:text="Ellipsize 연습"
    android:textSize="15dp" />
```

- - -