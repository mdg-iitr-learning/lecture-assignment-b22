using UnityEngine;

public class targetBehaviour : MonoBehaviour {


  
   public void Replace()
    {

        gameObject.transform.position= new Vector3(Random.Range(-5.0f, 5.0f), Random.Range(3.2f, 5.0f), Random.Range(-5.0f, 5.0f));
        gameObject.transform.localScale = Vector3.one * (float)Random.Range(1, 2f);
        gameObject.GetComponent<MeshRenderer>().material.color = Random.ColorHSV();
        print("it is working fine");
      }
}
