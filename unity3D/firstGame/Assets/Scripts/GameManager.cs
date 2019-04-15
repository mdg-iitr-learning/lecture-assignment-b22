using UnityEngine;
using UnityEngine.SceneManagement; //to change or reload scenes

public class GameManager : MonoBehaviour {
    bool gameEnded = false;
    public GameObject completelevelUI;
    public void CompleteLevel()
    {
        completelevelUI.SetActive(true);
    }
	public void EndGame()
    {
        if (gameEnded == false)
        {
            gameEnded = true;
            Invoke("Restart",2f);
        }
    }
    void Restart()
    {
        SceneManager.LoadScene(SceneManager.GetActiveScene().name);
    }
}
