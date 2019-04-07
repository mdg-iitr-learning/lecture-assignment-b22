using UnityEngine;

public class PlayerMovement : MonoBehaviour {
    public Rigidbody rb;
    public float fforce = 2000f;
    public float sforce = 500f;


	// Use this for initialization
	void Start () {
        //runs when game starts

	}
	
	// Update is called once per frame
	void FixedUpdate () {  //fixed: used when physics stuff added to objects
        rb.AddForce(0, 0, fforce*Time.deltaTime); //Time.deltatime to bring all computers at par wrt change in frames

        if(Input.GetKey("d"))
        {
            rb.AddForce(sforce* Time.deltaTime, 0, 0, ForceMode.VelocityChange);
        }
        if (Input.GetKey("a"))
        {
            rb.AddForce(-sforce* Time.deltaTime, 0, 0, ForceMode.VelocityChange);
        }
        if (rb.position.y < 0.5)
        {
            FindObjectOfType<GameManager>().EndGame();
        }

    }
}
