using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class controller : MonoBehaviour { 

    public float mspeed;

    // Start is called before the first frame update
    void Start()
    {
        mspeed = 7f;
    }

    // Update is called once per frame
    private void Update()
    {
        transform.Translate(-mspeed * Input.GetAxis("Horizontal") * Time.deltaTime , 0f , -mspeed * Input.GetAxis("Vertical") * Time.deltaTime);
    }
}
