import java.util.Arrays;
import java.util.Random;
​
public class SortAlgorithm {
​
     // 랜덤 숫자 생성
     static void Rand (int arr[]) {
          Random rd = new Random();
​
          for (int i = 0; i < arr.length; i++) {
               arr[i] = rd.nextInt(9) + 1; // 0부터 9개만큼(0~8) 무작위 숫자 생성 +1 : 1~9
               
               // 숫자가 중복되지 않게 값 입력
               for (int j = 0; j < i; j++) {
                    if (arr[i] == arr[j])
                         i--;
               }
          }
     }
​
     // 버블 정렬
     static void BubbleSort(int arr[]) {

          int temp = 0; // 숫자 위치를 바꿀 때 잠깐 사용
          for (int i=0; i<arr.length; i++) { 
               for (int j=1; j<arr.length-i; j++) { // arr[1]부터 비교해야하므로 j=1 (0에서는 -1을 할 수 없음)
                    
                    // 현재 숫자가 앞에 있는 숫자보다 작으면 자리 바꾸기
                    if (arr[j] < arr[j-1]) { 
                         temp = arr[j-1];
                         arr[j-1] = arr[j]; 
                         arr[j] = temp; 
                    } 
               }
          }
     }
​
     public static void main(String[] args) {

          int[] arr = new int[9];
          Rand(arr);​
          System.out.println("정렬 전 : "+Arrays.toString(arr));
          BubbleSort(arr);
          System.out.println("정렬 후 : "+Arrays.toString(arr));
     }
}