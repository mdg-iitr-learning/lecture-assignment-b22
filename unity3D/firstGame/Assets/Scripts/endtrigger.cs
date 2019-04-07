using UnityEngine;

public class endtrigger : MonoBehaviour {
    public GameManager gameManager;
	void OnTriggerEnter()
    {
        gameManager.CompleteLevel();
    }
}
