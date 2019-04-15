using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class reticleTest : MonoBehaviour
{ 
    public void RandomlyTeleport()
    {
        gameObject.transform.position = new Vector3(GetRandomCoordinate(), Random.Range(.5f, 2), GetRandomCoordinate());
    }

    private float GetRandomCoordinate()
    {
        var cordinate = Random.Range(-7f, 7f);
        while(cordinate > -1.5f && cordinate < 1.5f)
        {
            cordinate = Random.Range(-5, 5);
        }
        return cordinate;
    }
}
