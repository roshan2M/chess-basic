/*****

The ChessBasic Class

BY: Roshan Munjal
CLASS: ICS 3U0
TEACHER: Ms. Megson
START DATE: 16th May, 2016
LAST EDIT: 9th June, 2016

The purpose of this game is multiple-fold:
- To create a variation of chess including pawns, rooks, and kings.
- To learn the logic behind such a complicated game such as chess. Although not all features will be included,
  it is a great way to learn to code a game, and is valuable experience for the future.
- To create a game that chess players can use to analyze end-game positions, where, usually, only some pieces
  such as pawns and rooks remain.
- In this modified version of chess, once the king of one player is captured (can be done in ChessBasic), he
  or she automatically wins the game. There is no concept of check, checkmate, or stalemate, as those were
  difficult to complete due to time constraints near the end of the semester.

The main methods used in this game are:
- The mouseClicked method was greatly used in this game to determine where the mouse was clicked, and what result
  that had in the game. Due to the several different screens used in the program, each one had to be designated a
  boolean variable that could be checked for. Thus, if the game screen variable was true, then the game screen
  would appear on the sceen.
- Another widely used method was the paint method, since, depending on the screen that was active, a different
  screen had to be painted by the program. Thus, this took a lot of time and effort to articulate.
- Furthermore, the movePiece method, that I developed myself, played an integral role in the actual function of
  pieces on the board. This was vital because it related to all the different move checkers for all the different
  pieces, it related to the mouseClicked method as the possible moves to be clicked had to be determined, it
  determined if a piece had moved or not, which player's turn it was, etc.
- The returnPos method was one of the first methods that I developed and was important to click on the pieces
  on the board. It allowed me to develop the basics of the game.

Additional notes:
- All the code has been carefully segmented into the various different methods, each of which have a different
  function.
- All the screens have a different function and have been linked in all the methods that they need to be linked
  in.
- NOTE: When exiting the program, please exit using the exit button in the top-right corner to save the results
  properly.
- ENJOY THE GAME!

*****/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChessBasic extends Applet implements MouseListener, MouseMotionListener, KeyListener, Runnable // All the implementations that are needed.
{
    /*****

    APPLET SECTION

    This contains of all of the information and code pertaining to the applet, such as:
    - Drawing the screens on the applet.
    - Inserting all the methods needed for the applet.
    - Creating all the original methods related to applets.

    *****/

    // Place instance variables here.
    // Initialization of variables.
    // All of these variables are global variables, which means that they can be altered and accessed from anywhere in the class.

    // These variables initialize the size of the screen.
    static int appletsize_x = 1000;
    static int appletsize_y = 800;

    // These boolean variables are created to indicate which screens are initially turned on and which screens are turned off (true and false, respectively).
    static boolean mainMenu = true;
    static boolean gameScreen = false;
    static boolean rulesOfGame = false;
    static boolean resultsPage = false;

    // These boolean variables are created to ndicate which screens have been hovered on - or which buttons have had their
    static boolean mainMenuHover = false;
    static boolean gameScreenHover = false;
    static boolean rulesOfGameHover = false;
    static boolean resultsPageHover = false;
    static boolean resetHover = false;

    // These variables initialize the first mouse click (mx, my), and the second mouse click (nx, ny).
    // The mouseWasClicked variable determines if the mouse had already been clicked on the screen, during the chess game.
    // The piecePosOriginal variable is the position of where the mouse was first clicked, as a variable referring to the main 2 dimensional array of the board.
    // The piecePosNew variable is the position of where the mouse was clicked secondly, as a variable referring to the main 2 dimensional array of the board.
    // The numberOfMoves variable is used to count how many moves have passed, and to determine which player's turn it is on the board.
    // The blackHasWon and whiteHasWon variables are used to determine if one player has won or not.
    // The numberOfWins array is used to store how many times white and black have won, respectively.
    // The invalidMoveClicks int is used to check if a valid square was clicked on when a piece is dropped.
    static int mx, my, nx, ny, mouseX, mouseY, mouseMovedX, mouseMovedY, pieceTemp, invalidMoveClicks;
    static int mouseWasClicked = -1;
    static int[] piecePosOriginal = new int [2];
    static int[] piecePosNew = new int [2];
    static int numberOfMoves = 0;
    static boolean blackHasWon, whiteHasWon;
    static int[] numberOfWins = new int [2];

    // The x_pos and y_pos variables determine the left-most and upper-most position on the board, and draw everything according to that.
    // The indent variable is used to indent all the piece pictures by 5 units, and the size variable determines the size of each square.
    static int x_pos = 120;
    static int y_pos = 120;
    static int indent = 5;
    static int size = 70;

    // This determines the locations of buttons on the game screen.
    static int button_x_pos = 690;
    static int button_y_pos = 270;

    // The availableMoves variable stores the moves that are available to the piece that has been selected in the chess game.
    // The counterOfMoves variable stores how many moves are actually possible.
    // The rookCanMove variable refers to the condition that the rook can still move in a certain direction. Counter refers to the amount that the rook can move.
    // The counter is used to count how many times the rook can move in a certain direction.
    static int[] [] availableMoves = new int [50] [2];
    static int counterOfMoves;
    static boolean rookCanMove;
    static int counter;

    // Declare two instance variables at the head of the program.
    // If needed these instance vaiables might be used.
    MediaTracker tr;
    private Image dbImage;
    private Graphics dbg;

    // This is the 2 dimensional array that initializes all the pieces on the board.
    //  1 - white pawn,  2 - white rook,  3 - white king
    // 11 - black pawn, 12 - black rook, 13 - black king
    static int[] [] chessBoardArray = {{12, 0, 0, 0, 13, 0, 0, 12},
	    {11, 11, 11, 11, 11, 11, 11, 11},
	    {0, 0, 0, 0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0, 0, 0, 0},
	    {1, 1, 1, 1, 1, 1, 1, 1},
	    {2, 0, 0, 0, 3, 0, 0, 2}};


    // Creates media tracker and image variables for images.
    // Images imported from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
    // Each image is needed for each type of piece.
    // 1 - black king, 2 - white king
    // 3 - black pawn, 4 - white pawn
    // 5 - black rook, 6 - white rook
    Image img1, img2, img3, img4, img5, img6, img7, img8, img9;
    Image button1, button2, button3, button4, button5, button6, button7;
    Image buttonLight1, buttonLight2, buttonLight3, buttonLight4, buttonLight5, buttonLight6, buttonLight7;
    int picWidth, picHeight;

    /*
	This method initializes all the pictures, adds media trackers (if necessary), and yields an error message if some of the media cannot be displayed properly.
	It is essentially to displaying everything on the screen, and vital for the rest of the code.
    */
    public void init ()
    {
	// Place the body of the initialization method here.
	// The mouse listener is required to sense the clicks of the mouse, for the purposes of ChessBasic.
	// The resize method resizes the screen to 1000 by 1000 pixels.
	// The request focus method requests the screen to already be the file in focus.
	// The set background method sets the colour of the background.
	addMouseListener (this);
	addMouseMotionListener (this);
	resize (appletsize_x, appletsize_y);
	requestFocus ();
	setBackground (new Color (255, 255, 204));

	// These images are imported from the file where stored (same folder as ChessBasic).
	img1 = getToolkit ().getImage ("Images/Pieces/Chess_King_Black.png");
	img2 = getToolkit ().getImage ("Images/Pieces/Chess_King_White.png");
	img3 = getToolkit ().getImage ("Images/Pieces/Chess_Pawn_Black.png");
	img4 = getToolkit ().getImage ("Images/Pieces/Chess_Pawn_White.png");
	img5 = getToolkit ().getImage ("Images/Pieces/Chess_Rook_Black.png");
	img6 = getToolkit ().getImage ("Images/Pieces/Chess_Rook_White.png");
	img7 = getToolkit ().getImage ("Images/Pieces/Chess_King_Large.png");
	img8 = getToolkit ().getImage ("Images/Pieces/Chess_Pawn_White_Large.png");
	img9 = getToolkit ().getImage ("Images/Pieces/Chess_Pawn_Black_Large.png");
	button1 = getToolkit ().getImage ("Images/Buttons/Main Menu.png");
	button2 = getToolkit ().getImage ("Images/Buttons/Rules of the Game.png");
	button3 = getToolkit ().getImage ("Images/Buttons/Results.png");
	button4 = getToolkit ().getImage ("Images/Buttons/Begin.png");
	button5 = getToolkit ().getImage ("Images/Buttons/Play Game.png");
	button6 = getToolkit ().getImage ("Images/Buttons/Reset.png");
	button7 = getToolkit ().getImage ("Images/Buttons/Continue.png");
	buttonLight1 = getToolkit ().getImage ("Images/Buttons/Main Menu_Light.png");
	buttonLight2 = getToolkit ().getImage ("Images/Buttons/Rules of the Game_Light.png");
	buttonLight3 = getToolkit ().getImage ("Images/Buttons/Results_Light.png");
	buttonLight4 = getToolkit ().getImage ("Images/Buttons/Begin_Light.png");
	buttonLight5 = getToolkit ().getImage ("Images/Buttons/Play Game_Light.png");
	buttonLight6 = getToolkit ().getImage ("Images/Buttons/Reset_Light.png");
	buttonLight7 = getToolkit ().getImage ("Images/Buttons/Continue_Light.png");

	// These media tracker was created to track all the images on the screen.
	MediaTracker tracker1 = new MediaTracker (this);
	tracker1.addImage (img1, 0);
	tracker1.addImage (img2, 0);
	tracker1.addImage (img3, 0);
	tracker1.addImage (img4, 0);
	tracker1.addImage (img5, 0);
	tracker1.addImage (img6, 0);
	tracker1.addImage (img7, 0);
	tracker1.addImage (img8, 0);
	tracker1.addImage (img9, 0);
	tracker1.addImage (button1, 0);
	tracker1.addImage (button2, 0);
	tracker1.addImage (button3, 0);
	tracker1.addImage (button4, 0);
	tracker1.addImage (button5, 0);
	tracker1.addImage (button6, 0);
	tracker1.addImage (button7, 0);
	tracker1.addImage (buttonLight1, 0);
	tracker1.addImage (buttonLight2, 0);
	tracker1.addImage (buttonLight3, 0);
	tracker1.addImage (buttonLight4, 0);
	tracker1.addImage (buttonLight5, 0);
	tracker1.addImage (buttonLight6, 0);
	tracker1.addImage (buttonLight7, 0);

	// If the media tracker is needed, then the tracker would run this code to wait for the images.
	try
	{
	    tracker1.waitForAll ();
	}

	catch (InterruptedException e)
	{
	}

	// The following try loop reads the file called "Win Count for White and Black.txt", accesses the information needed, and closes the file.
	try
	{
	    FileReader fileReader = new FileReader ("Documents/Win Count for White and Black.txt");
	    BufferedReader bufferedReader = new BufferedReader (fileReader);
	    numberOfWins [0] = Integer.parseInt (bufferedReader.readLine ());
	    numberOfWins [1] = Integer.parseInt (bufferedReader.readLine ());
	    bufferedReader.close ();
	}
	catch (IOException e)
	{
	    System.out.print ("Could not read file.");
	}

	// If there were any errors loading the image, then abort the program with a message.
	if (tracker1.isErrorAny ())
	{
	    showStatus ("Could not load one of the images.");
	    return;
	}
    } // init method


    public void start ()
    {
	// Define a new thread.
	Thread th = new Thread (this);
	// Start this thread.
	th.start ();
    }


    public void stop ()
    {
	// Nothing as of now. Will be filled if needed.
    }


    public void destroy ()
    {
	// Nothing as of now. Will be filled if needed.
    }


    public void run ()
    {
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The mouseClicked method is activated whenever the mouse is entered.
	@ param MouseEvent e : Recieves the location of the mouse in relation to the applet screen.
    */
    public void mouseEntered (MouseEvent e)
    {
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The mouseClicked method is activated whenever the mouse is exited.
	@ param MouseEvent e : Recieves the location of the mouse in relation to the applet screen.
    */
    public void mouseExited (MouseEvent e)
    {
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The mouseClicked method is activated whenever the mouse is clicked.
	@ param MouseEvent e : Recieves the location of the mouse in relation to the applet screen.
    */
    public void mouseClicked (MouseEvent e)
    {
	// The mouseX and mouseY variables are needed for when the player is outside the game screen.
	mouseX = e.getX ();
	mouseY = e.getY ();

	// This makes it so that once a button is clicked, none of the hover booleans are stil true.
	mainMenuHover = false;
	gameScreenHover = false;
	rulesOfGameHover = false;
	resultsPageHover = false;
	resetHover = false;

	// If the game screen is true, then the following mouse clicks are available.
	if (gameScreen == true)
	{
	    // If the mouse is clicked in the range of the main menu screen button, then the main menu is turned true.
	    // Also, the board is cleared of pieces and all variables in the game are reset.
	    if (mouseX >= button_x_pos && mouseX <= button_x_pos + button1.getWidth (null) && mouseY >= button_y_pos && mouseY <= button_y_pos + button1.getHeight (null))
	    {
		mainMenu = true;
		gameScreen = false;

		for (int i = 0 ; i < 8 ; i++)
		{
		    for (int j = 0 ; j < 8 ; j++)
		    {
			chessBoardArray [i] [j] = 0;
		    }
		}

		chessBoardArray [0] [0] = 12;
		chessBoardArray [0] [4] = 13;
		chessBoardArray [0] [7] = 12;
		for (int i = 0 ; i < 8 ; i++)
		{
		    chessBoardArray [1] [i] = 11;
		}

		chessBoardArray [7] [0] = 2;
		chessBoardArray [7] [4] = 3;
		chessBoardArray [7] [7] = 2;
		for (int i = 0 ; i < 8 ; i++)
		{
		    chessBoardArray [6] [i] = 1;
		}

		whiteHasWon = false;
		blackHasWon = false;

		mouseWasClicked = -1;
		numberOfMoves = 0;
	    }
	    // If the mouse is clicked on the rules of game button, then the rules of game variable is set true.
	    else if (mouseX >= button_x_pos && mouseX <= button_x_pos + button2.getWidth (null) && mouseY >= button_y_pos + 2 * size && mouseY <= button_y_pos + 2 * size + button2.getHeight (null))
	    {
		gameScreen = false;
		rulesOfGame = true;
	    }
	    // If the mouse is clicked on the results button, then the results variable is set true.
	    else if (mouseX >= button_x_pos && mouseX <= button_x_pos + button3.getWidth (null) && mouseY >= button_y_pos + 4 * size && mouseY <= button_y_pos + 4 * size + button3.getHeight (null))
	    {
		gameScreen = false;
		resultsPage = true;
	    }
	    // If the mouse is clicked on the exit button, then the game is exited.
	    // However, beforehand, the number of games won by white and black are updated in the file previously opened.
	    else if (mouseX >= appletsize_x - 100 && mouseX <= appletsize_x && mouseY >= 0 && mouseY <= 40)
	    {
		// This try loop accesses the file and overwrites it as needed.
		try
		{
		    FileWriter fileWriter = new FileWriter ("Documents/Win Count for White and Black.txt");
		    PrintWriter printWriter = new PrintWriter (fileWriter);
		    printWriter.println (numberOfWins [0]);
		    printWriter.println (numberOfWins [1]);
		    fileWriter.close ();
		}
		catch (IOException ex)
		{
		    System.out.print ("The file could not be updated.");
		}

		// Exits the system.
		System.exit (0);
	    }

	    // If no piece has currently been clicked on, the following code is executed.
	    if (mouseWasClicked == -1 || mouseWasClicked == 0)
	    {
		mx = e.getX (); // Gets the x- coordinate of the mouse's location. Needed in the chess game.
		my = e.getY (); // Gets the y- coordinate of the mouse's location. Needed in the chess game.

		// If the mouse is clicked within the board, and if neither side has won yet, then the user can move the piece.
		if (mx >= x_pos && mx <= x_pos + 8 * size && my >= y_pos && my <= y_pos + 8 * size)
		{
		    if (whiteHasWon == false && blackHasWon == false)
		    {
			movePiece (); // Calls the method movePiece to call all the logic of moving the pieces on the board.
		    }
		}
	    }
	    // If a piece has been selected on the board, then the following code is executed.
	    else if (mouseWasClicked == 1)
	    {
		nx = e.getX (); // Gets the x- coordinate of the mouse's location.
		ny = e.getY (); // Gets the y- coordinate of the mouse's location.

		// If the mouse is clicked on the board, then the following is run.
		if (nx >= x_pos && nx <= x_pos + 8 * size && ny >= y_pos && ny <= y_pos + 8 * size)
		{
		    // If white or black have not already won, then the player can move the piece to the new location.
		    if (whiteHasWon == false && blackHasWon == false)
		    {
			movePiece (); // Calls the method movePiece to call all the logic of moving the pieces on the board.
		    }
		}
	    }
	}
	// Otherwise, if the main menu screen is on, the following mouse click commands are possible.
	else if (mainMenu == true)
	{
	    // If the user clicks on the rules of game button, then the rules of game variable is set true.
	    if (mouseX >= 30 && mouseX <= 30 + button2.getWidth (null) && mouseY >= 300 && mouseY <= 300 + button2.getHeight (null))
	    {
		mainMenu = false;
		rulesOfGame = true;
	    }
	    // If the user clicks on the results button, then the results variable is set true.
	    else if (mouseX >= 670 && mouseX <= 670 + button3.getWidth (null) && mouseY >= 300 && mouseY <= 300 + button3.getHeight (null))
	    {
		mainMenu = false;
		resultsPage = true;
	    }
	    // If the user clicks on the game screen button, then the game screen variable is set true.
	    else if (mouseX >= 275 && mouseX <= 275 + button4.getWidth (null) && mouseY >= 670 && mouseY <= 670 + button4.getHeight (null))
	    {
		mainMenu = false;
		gameScreen = true;
	    }
	    // If the user clicks on the exit button, then the program is exited.
	    else if (mouseX >= appletsize_x - 100 && mouseX <= appletsize_x && mouseY >= 0 && mouseY <= 40)
	    {
		// Before closing, the program also overwrites the file with the new number of times that black and white have won.
		try
		{
		    FileWriter fileWriter = new FileWriter ("Documents/Win Count for White and Black.txt");
		    PrintWriter printWriter = new PrintWriter (fileWriter);
		    printWriter.println (numberOfWins [0]);
		    printWriter.println (numberOfWins [1]);
		    fileWriter.close ();
		}
		catch (IOException ex)
		{
		    System.out.print ("The file could not be updated.");
		}

		System.exit (0);
	    }
	}
	// If the rules of the game variable is true, the following mouse commands are possible.
	else if (rulesOfGame == true)
	{
	    // If the user clicks on the exit button, then the program is exited.
	    if (mouseX >= appletsize_x - 100 && mouseX <= appletsize_x && mouseY >= 0 && mouseY <= 40)
	    {
		// The file is overwritten with the new number of times that black and white have won.
		try
		{
		    FileWriter fileWriter = new FileWriter ("Documents/Win Count for White and Black.txt");
		    PrintWriter printWriter = new PrintWriter (fileWriter);
		    printWriter.println (numberOfWins [0]);
		    printWriter.println (numberOfWins [1]);
		    fileWriter.close ();
		}
		catch (IOException ex)
		{
		    System.out.print ("The file could not be updated.");
		}

		System.exit (0);
	    }
	    // If the user clicks on the game screen button, then the game screen variable is set true.
	    else if (mouseX >= 100 && mouseX <= 100 + button5.getWidth (null) && mouseY >= 650 && mouseY <= 650 + button5.getHeight (null))
	    {
		gameScreen = true;
		rulesOfGame = false;
	    }
	    // If the user clicks on the main menu button, then the main menu variable is set true.
	    // Also, the chess board is cleared out.
	    else if (mouseX >= 340 && mouseX <= 340 + button6.getWidth (null) && mouseY >= 580 && mouseY <= 580 + button6.getHeight (null))
	    {
		mainMenu = true;
		rulesOfGame = false;

		for (int i = 0 ; i < 8 ; i++)
		{
		    for (int j = 0 ; j < 8 ; j++)
		    {
			chessBoardArray [i] [j] = 0;
		    }
		}

		chessBoardArray [0] [0] = 12;
		chessBoardArray [0] [4] = 13;
		chessBoardArray [0] [7] = 12;
		for (int i = 0 ; i < 8 ; i++)
		{
		    chessBoardArray [1] [i] = 11;
		}

		chessBoardArray [7] [0] = 2;
		chessBoardArray [7] [4] = 3;
		chessBoardArray [7] [7] = 2;
		for (int i = 0 ; i < 8 ; i++)
		{
		    chessBoardArray [6] [i] = 1;
		}

		whiteHasWon = false;
		blackHasWon = false;

		mouseWasClicked = -1;
		numberOfMoves = 0;
	    }
	    // If the results button is clicked, then the results variable is set to true and the screen is displayed.
	    else if (mouseX >= appletsize_x - 400 && mouseX <= appletsize_x - 400 + button1.getWidth (null) && mouseY >= 650 && mouseY <= 650 + button1.getHeight (null))
	    {
		rulesOfGame = false;
		resultsPage = true;
	    }
	}
	// If the results variable is true, then the results page is displayed.
	else if (resultsPage == true)
	{
	    // If the user clicks on the exit button, then the program is exited.
	    if (mouseX >= appletsize_x - 100 && mouseX <= appletsize_x && mouseY >= 0 && mouseY <= 40)
	    {
		// The file is overwritten with the new number of times that black and white have won.
		try
		{
		    FileWriter fileWriter = new FileWriter ("Documents/Win Count for White and Black.txt");
		    PrintWriter printWriter = new PrintWriter (fileWriter);
		    printWriter.println (numberOfWins [0]);
		    printWriter.println (numberOfWins [1]);
		    fileWriter.close ();
		}
		catch (IOException ex)
		{
		    System.out.print ("The file could not be updated.");
		}

		System.exit (0);
	    }
	    // If the user clicks on the game screen button, then the game screen variable is set true.
	    else if (mouseX >= 100 && mouseX <= 100 + button5.getWidth (null) && mouseY >= 650 && mouseY <= 650 + button5.getHeight (null))
	    {
		gameScreen = true;
		resultsPage = false;
	    }
	    // If the user clicks on the main menu button, then the main menu variable is set true.
	    // Also, the chess board is cleared out.
	    else if (mouseX >= appletsize_x - 400 && mouseX <= appletsize_x - 400 + button1.getWidth (null) && mouseY >= 650 && mouseY <= 650 + button1.getHeight (null))
	    {
		mainMenu = true;
		resultsPage = false;

		for (int i = 0 ; i < 8 ; i++)
		{
		    for (int j = 0 ; j < 8 ; j++)
		    {
			chessBoardArray [i] [j] = 0;
		    }
		}

		chessBoardArray [0] [0] = 12;
		chessBoardArray [0] [4] = 13;
		chessBoardArray [0] [7] = 12;
		for (int i = 0 ; i < 8 ; i++)
		{
		    chessBoardArray [1] [i] = 11;
		}

		chessBoardArray [7] [0] = 2;
		chessBoardArray [7] [4] = 3;
		chessBoardArray [7] [7] = 2;
		for (int i = 0 ; i < 8 ; i++)
		{
		    chessBoardArray [6] [i] = 1;
		}

		whiteHasWon = false;
		blackHasWon = false;

		mouseWasClicked = -1;
		numberOfMoves = 0;
	    }
	    // If the reset button is clicked, then the number of wins for both white and black is set to 0.
	    else if (mouseX >= 340 && mouseX <= 340 + button6.getWidth (null) && mouseY >= 580 && mouseY <= 580 + button6.getHeight (null))
	    {
		numberOfWins [0] = 0;
		numberOfWins [1] = 0;
	    }
	}
	repaint (); // Repaints the screen.
	e.consume (); // Stops running the mouse click method.
    }


    /*
	The mousePressed method detects when the mouse button is pressed and coresponds them to different actions.
	@ param MouseEvent e : Recieves the location of the mouse in relation to the applet screen.
    */
    public void mousePressed (MouseEvent e)
    {
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The mouseReleased method is activated whenever the mouse is released after being pressed.
	@ param MouseEvent e : Recieves the location of the mouse in relation to the applet screen.
    */
    public void mouseReleased (MouseEvent e)
    {
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The mouseMoved method is activated whenever the mouse is moved on the screen.
	@ param MouseEvent e : Recieves the location of the mouse in relation to the applet screen.
    */
    public void mouseMoved (MouseEvent e)
    { // Called during motion when no buttons are down.
	mouseMovedX = e.getX ();
	mouseMovedY = e.getY ();

	// Presets it so that no button is being hovered over.
	mainMenuHover = false;
	gameScreenHover = false;
	rulesOfGameHover = false;
	resultsPageHover = false;
	resetHover = false;

	// If the main menu is on the screen.
	if (mainMenu == true)
	{
	    // If the user hovers over the rules of game button.
	    if (mouseMovedX >= 30 && mouseMovedX <= 30 + button2.getWidth (null) && mouseMovedY >= 300 && mouseMovedY <= 300 + button2.getHeight (null))
	    {
		rulesOfGameHover = true;
	    }
	    // If the user hovers over the results button.
	    else if (mouseMovedX >= 670 && mouseMovedX <= 670 + button3.getWidth (null) && mouseMovedY >= 300 && mouseMovedY <= 300 + button3.getHeight (null))
	    {
		resultsPageHover = true;
	    }
	    // If the user hovers over the game screen button.
	    else if (mouseMovedX >= 275 && mouseMovedX <= 275 + button4.getWidth (null) && mouseMovedY >= 670 && mouseMovedY <= 670 + button4.getHeight (null))
	    {
		gameScreenHover = true;
	    }
	}
	// If the game screen is on the screen.
	else if (gameScreen == true)
	{
	    // If the user hovers over the main menu of game button.
	    if (mouseMovedX >= button_x_pos && mouseMovedX <= button_x_pos + button1.getWidth (null) && mouseMovedY >= button_y_pos && mouseMovedY <= button_y_pos + button1.getHeight (null))
	    {
		mainMenuHover = true;
	    }
	    // If the user hovers over the rules of the game button.
	    else if (mouseMovedX >= button_x_pos && mouseMovedX <= button_x_pos + button1.getWidth (null) && mouseMovedY >= button_y_pos + 2 * size && mouseMovedY <= button_y_pos + 2 * size + button1.getHeight (null))
	    {
		rulesOfGameHover = true;
	    }
	    // If the user hovers over the results button.
	    else if (mouseMovedX >= button_x_pos && mouseMovedX <= button_x_pos + button1.getWidth (null) && mouseMovedY >= button_y_pos + 4 * size && mouseMovedY <= button_y_pos + 4 * size + button1.getHeight (null))
	    {
		resultsPageHover = true;
	    }
	}
	// If the rule of the game are on the screen.
	else if (rulesOfGame == true)
	{
	    // If the user hovers over the game screen button.
	    if (mouseMovedX >= 100 && mouseMovedX <= 100 + button5.getWidth (null) && mouseMovedY >= 650 && mouseMovedY <= 650 + button5.getHeight (null))
	    {
		gameScreenHover = true;
	    }
	    // If the user hovers over the main menu button.
	    else if (mouseMovedX >= 340 && mouseMovedX <= 340 + button6.getWidth (null) && mouseMovedY >= 580 && mouseMovedY <= 580 + button6.getHeight (null))
	    {
		mainMenuHover = true;
	    }
	    // If the user hovers over the results button.
	    else if (mouseMovedX >= appletsize_x - 400 && mouseMovedX <= appletsize_x - 400 + button1.getWidth (null) && mouseMovedY >= 650 && mouseMovedY <= 650 + button1.getHeight (null))
	    {
		resultsPageHover = true;
	    }

	}
	// If the results are on the screen.
	else if (resultsPage == true)
	{
	    // If the user hovers over the game screen button.
	    if (mouseMovedX >= 100 && mouseMovedX <= 100 + button5.getWidth (null) && mouseMovedY >= 650 && mouseMovedY <= 650 + button5.getHeight (null))
	    {
		gameScreenHover = true;
	    }
	    // If the user hovers over the reset button.
	    else if (mouseMovedX >= 340 && mouseMovedX <= 340 + button6.getWidth (null) && mouseMovedY >= 580 && mouseMovedY <= 580 + button6.getHeight (null))
	    {
		resetHover = true;
	    }
	    // If the user hovers over the main menu button.
	    else if (mouseMovedX >= appletsize_x - 400 && mouseMovedX <= appletsize_x - 400 + button1.getWidth (null) && mouseMovedY >= 650 && mouseMovedY <= 650 + button1.getHeight (null))
	    {
		mainMenuHover = true;
	    }
	}
	repaint ();
	e.consume ();
    }


    /*
	The mouseDragged method is activated whenever the mouse is dragged with the click down.
	@ param MouseEvent e : Recieves the location of the mouse in relation to the applet screen.
    */
    public void mouseDragged (MouseEvent e)
    { // Called during motion with buttons down.
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The keyTyped method is activated whenever a key is typed.
	@ param KeyEvent e : Recieves the ASCII code value of the key character that was pressed.
    */
    public void keyTyped (KeyEvent e)
    {
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The keyPressed method is activated whenever a key is pressed.
	@ param KeyEvent e : Recieves the ASCII code value of the key character that was pressed.
    */
    public void keyPressed (KeyEvent e)
    {
	// Variable to store the ASCII code values.
	int key = e.getKeyCode ();
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The keyReleased method is activated whenever a key is released.
	@ param KeyEvent e : Recieves the ASCII code value of the key character that was pressed.
    */
    public void keyReleased (KeyEvent e)
    {
	// Nothing as of now. Will be filled if needed.
    }


    /*
	The update method updates the graphics on the screen without having the
    */
    public void update (Graphics g)
    {
	// Initializes the buffer.
	if (dbImage == null)
	{
	    dbImage = createImage (this.getSize ().width, this.getSize ().height);
	    dbg = dbImage.getGraphics ();
	}

	// Clears the screen in the background.
	dbg.setColor (getBackground ());
	dbg.fillRect (0, 0, this.getSize ().width, this.getSize ().height);

	// Draws the elements in the background.
	dbg.setColor (getForeground ());
	paint (dbg);

	// Draws an image on the screen.
	g.drawImage (dbImage, 0, 0, this);
    }


    /*
	This method draws shapes and images, puts colours on the screen, and updates the screen as needed - update method not needed - at least for chess.
	@ param Graphics g : The graphics that should be yielded are given by the commands that g is given in the method.
    */
    public void paint (Graphics g)
    {
	// If the game screen is on, then the following graphics are displayed.
	if (gameScreen == true)
	{
	    // Draws the text on top that displays the name of the program.
	    g.setColor (new Color (255, 153, 51));
	    g.setFont (new Font ("Footlight MT Light", 0, 120));
	    g.drawString ("ChessBasic", 120, 100);

	    // This checks if either of the players has won. If so, then it says so at the bottom of the screen.
	    g.setColor (Color.BLACK);
	    g.setFont (new Font ("Algerian", 0, 80));

	    // If white has won, it displays the text written below.
	    if (whiteHasWon == true)
	    {
		g.drawString ("White Wins!", 180, 760);
	    }
	    // If black has won, it displays the text written below.
	    else if (blackHasWon == true)
	    {
		g.drawString ("Black Wins!", 180, 760);
	    }

	    // This code draws a square in the top-left corner of the screen.
	    g.setColor (new Color (255, 153, 51));
	    g.drawRect (40, y_pos, size, size);

	    // The colour of the square indicates if it is white's move or black's move.
	    if (numberOfMoves % 2 == 0)
	    {
		g.setColor (Color.WHITE);
		g.fillRect (41, y_pos + 1, size - 1, size - 1);
	    }
	    else if (numberOfMoves % 2 == 1)
	    {
		g.setColor (Color.BLACK);
		g.fillRect (41, y_pos + 1, size - 1, size - 1);
	    }

	    g.setColor (new Color (255, 153, 51));
	    // This section of code draws the chess board.
	    // It is just a background to place all the pieces on the board.
	    for (int j = 0 ; j < 8 ; j++)
	    { // This is to print all the rows.
		for (int i = 0 ; i < 8 ; i++)
		{ // This is to print all the columns.
		    g.drawRect (x_pos + (size * i), y_pos + (size * j), size, size);
		    if (j % 2 == 1)
		    {
			if (i % 2 == 0)
			    // This is because alternate squares need to be filled.
			    g.fillRect (x_pos + (size * i), y_pos + (size * j), size, size);
		    }
		    else if (j % 2 == 0)
		    {
			if (i % 2 == 1)
			    // This is contrary to the last if statement, so that alternate colour squares are filled.
			    g.fillRect (x_pos + (size * i), y_pos + (size * j), size, size);
		    }
		}
	    }

	    // This section of code highlights squares when needed.
	    // In the second click, red squares are highlighted to indicate the location that the piece was dropped in.
	    if (mouseWasClicked == 0)
	    {
		// If the square clicked on was valid, and thus produced 1 less invalid move click checks, then the following code is executed.
		if (invalidMoveClicks < counterOfMoves)
		{
		    g.setColor (Color.RED); // Sets the colour to red to indicate where the piece was dropped.
		    g.fillRect (x_pos + piecePosNew [1] * size + 1, y_pos + piecePosNew [0] * size + 1, size - 1, size - 1); // Draws the highlighted square on the board.
		}
	    }
	    // In the first click, a blue square is highlighted along with all the squares that the piece can move to.
	    else if (mouseWasClicked == 1)
	    {
		g.setColor (Color.BLUE); // Sets the colour to blue to indicate where the piece was clicked and where the piece can move on the board.
		g.fillRect (x_pos + piecePosOriginal [1] * size + 1, y_pos + piecePosOriginal [0] * size + 1, size - 1, size - 1); // Draws the highlighted squares on the board.

		// This for statement goes through each of the possible moves for the piece and highlights the squares that are available.
		for (int i = 0 ; i < counterOfMoves ; i++)
		{
		    g.fillRect (x_pos + size * (availableMoves [i] [1]) + 1, y_pos + size * (availableMoves [i] [0]) + 1, size - 1, size - 1);
		}
	    }

	    // This section of code draws all the pieces on the board.
	    // It checks the location of each piece and draws it according to where it is on the board.
	    for (int i = 0 ; i < 8 ; i++)
	    {
		for (int j = 0 ; j < 8 ; j++)
		{
		    if (chessBoardArray [i] [j] == 1)
			g.drawImage (img4, x_pos + indent + j * size, y_pos + indent + i * size, null);
		    else if (chessBoardArray [i] [j] == 2)
			g.drawImage (img6, x_pos + indent + j * size, y_pos + indent + i * size, null);
		    else if (chessBoardArray [i] [j] == 3)
			g.drawImage (img2, x_pos + indent + j * size, y_pos + indent + i * size, null);
		    else if (chessBoardArray [i] [j] == 11)
			g.drawImage (img3, x_pos + indent + j * size, y_pos + indent + i * size, null);
		    else if (chessBoardArray [i] [j] == 12)
			g.drawImage (img5, x_pos + indent + j * size, y_pos + indent + i * size, null);
		    else if (chessBoardArray [i] [j] == 13)
			g.drawImage (img1, x_pos + indent + j * size, y_pos + indent + i * size, null);
		}
	    }

	    // This draws the buttons necessary on the game screen.
	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (mainMenuHover == true)
		g.drawImage (buttonLight1, button_x_pos, button_y_pos, null);
	    else
		g.drawImage (button1, button_x_pos, button_y_pos, null);

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (rulesOfGameHover == true)
		g.drawImage (buttonLight2, button_x_pos, button_y_pos + 2 * size, null);
	    else
		g.drawImage (button2, button_x_pos, button_y_pos + 2 * size, null);

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (resultsPageHover == true)
		g.drawImage (buttonLight3, button_x_pos, button_y_pos + 4 * size, null);
	    else
		g.drawImage (button3, button_x_pos, button_y_pos + 4 * size, null);

	    // This draws the rectangle for the exit button.
	    g.setColor (Color.GRAY);
	    g.fillRect (appletsize_x - 100, 0, 100, 40);

	    // This writes exit in the button.
	    g.setColor (Color.BLACK);
	    g.setFont (new Font ("Felix Titling", 0, 30));
	    g.drawString ("EXIT", appletsize_x - 80, 30);
	}
	// If the main menu is on, then the following graphics are displayed.
	else if (mainMenu == true)
	{
	    // Draws the text on top that displays the name of the program.
	    g.setColor (new Color (200, 153, 51));
	    g.setFont (new Font ("Calisto MT", 0, 90));
	    g.drawString ("ChessBasic", 300, 80);

	    // The title of main menu is displayed under the name of the program.
	    g.setFont (new Font ("Felix Titling", 0, 60));
	    g.drawString ("MAIN MENU", 345, 140);

	    // The large chess piece image is drawn.
	    g.drawImage (img7, 250, 150, null);

	    // The buttons for rules of the game, results, and begin are displayed, respectively.
	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (rulesOfGameHover == true)
		g.drawImage (buttonLight2, 30, 300, null);
	    else
		g.drawImage (button2, 30, 300, null);

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (resultsPageHover == true)
		g.drawImage (buttonLight3, 670, 300, null);
	    else
		g.drawImage (button3, 670, 300, null);

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (gameScreenHover == true)
		g.drawImage (buttonLight4, 275, 670, null);
	    else
		g.drawImage (button4, 275, 670, null);

	    // This draws the rectangle for the exit button.
	    g.setColor (Color.GRAY);
	    g.fillRect (appletsize_x - 100, 0, 100, 40);

	    // This writes exit in the button.
	    g.setColor (Color.BLACK);
	    g.setFont (new Font ("Felix Titling", 0, 30));
	    g.drawString ("EXIT", appletsize_x - 80, 30);
	}
	// If the rules of game screen is on, then the following graphics are displayed.
	else if (rulesOfGame == true)
	{
	    // Draws the text on top that displays the name of the program.
	    g.setColor (new Color (200, 153, 51));
	    g.setFont (new Font ("Calisto MT", 0, 90));
	    g.drawString ("ChessBasic", 300, 80);

	    // The title of rules of the game is displayed under the program name.
	    g.setFont (new Font ("Felix Titling", 0, 60));
	    g.drawString ("RULES OF THE GAME", 240, 140);

	    // This draws the rectangle for the exit button.
	    g.setColor (Color.GRAY);
	    g.fillRect (appletsize_x - 100, 0, 100, 40);

	    // This writes exit in the button.
	    g.setColor (Color.BLACK);
	    g.setFont (new Font ("Felix Titling", 0, 30));
	    g.drawString ("EXIT", appletsize_x - 80, 30);

	    // This draws the buttons for play game, main menu, and results, respectively.
	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (gameScreenHover == true)
	    {
		// If no moves have been played, then it displays "play game". Otherwise, it displays "continue".
		if (numberOfMoves == 0)
		    g.drawImage (buttonLight5, 100, 650, null);
		// If no moves have been played, then it displays "continue". Otherwise, it displays "continue".
		else
		    g.drawImage (buttonLight7, 100, 650, null);
	    }
	    else
	    {
		// If no moves have been played, then it displays "play game". Otherwise, it displays "continue".
		if (numberOfMoves == 0)
		    g.drawImage (button5, 100, 650, null);
		// If no moves have been played, then it displays "continue". Otherwise, it displays "continue".
		else
		    g.drawImage (button7, 100, 650, null);
	    }

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (mainMenuHover == true)
		g.drawImage (buttonLight1, 340, 580, null);
	    else
		g.drawImage (button1, 340, 580, null);

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (resultsPageHover == true)
		g.drawImage (buttonLight3, appletsize_x - 400, 650, null);
	    else
		g.drawImage (button3, appletsize_x - 400, 650, null);

	    // Sets the colour to an orange colour.
	    g.setColor (new Color (255, 140, 0));

	    // Lists the rules for the pawn.
	    g.setFont (new Font ("Calisto MT", 1, 30));
	    g.drawString ("PAWN RULES:", 20, 200);
	    g.setFont (new Font ("Calisto MT", 0, 20));
	    g.drawString ("1) A pawn can move forward by 1 square.", 20, 240);
	    g.drawString ("2) A pawn can move 2 squares forward if", 20, 270);
	    g.drawString ("    on the first move.", 20, 300);
	    g.drawString ("3) A pawn can capture diagonally if the", 20, 330);
	    g.drawString ("    piece is in the top-left or top-right corner.", 20, 360);
	    g.drawString ("4) When a pawn reaches the opposite side of the", 20, 390);
	    g.drawString ("    board, it is automatically promotes into a", 20, 420);
	    g.drawString ("    rook of that colour.", 20, 450);

	    // Lists the rules for the king.
	    g.setFont (new Font ("Calisto MT", 1, 30));
	    g.drawString ("KING RULES:", 580, 200);
	    g.setFont (new Font ("Calisto MT", 0, 20));
	    g.drawString ("1) A king can move one square in any direction.", 580, 240);
	    g.drawString ("2) Unlike in proper chess, the king may be", 580, 270);
	    g.drawString ("    captured. The game is over when the king", 580, 300);
	    g.drawString ("    has been captured by the opponent.", 580, 330);

	    // Lists the rules for the rook.
	    g.setFont (new Font ("Calisto MT", 1, 30));
	    g.drawString ("ROOK RULES:", 580, 390);
	    g.setFont (new Font ("Calisto MT", 0, 20));
	    g.drawString ("1) A rook can move as many squares in the", 580, 420);
	    g.drawString ("    horizontal and vertical directions, until", 580, 450);
	    g.drawString ("    the rook is blocked by another piece.", 580, 480);

	    // Lists a note about moving pieces on the board.
	    g.setFont (new Font ("Calisto MT", 1, 30));
	    g.drawString ("NOTE:", 20, 500);
	    g.setFont (new Font ("Calisto MT", 0, 20));
	    g.drawString ("To move a piece on the board, simply select it. To deselect the piece, simply click outside the squares highlighted", 20, 530);
	    g.drawString ("in blue. The box in the top-left corner indicates which player's move it is.", 20, 560);
	}
	// If the results screen is on, then the following graphics are displayed.
	else if (resultsPage == true)
	{
	    // Draws the text on top that displays the name of the program.
	    g.setColor (new Color (200, 153, 51));
	    g.setFont (new Font ("Calisto MT", 0, 90));
	    g.drawString ("ChessBasic", 300, 80);

	    // The title of results is displayed under the program name.
	    g.setFont (new Font ("Felix Titling", 0, 60));
	    g.drawString ("RESULTS", 360, 140);

	    // This draws the rectangle for the exit button.
	    g.setColor (Color.GRAY);
	    g.fillRect (appletsize_x - 100, 0, 100, 40);

	    // This writes exit in the button.
	    g.setColor (Color.BLACK);
	    g.setFont (new Font ("Felix Titling", 0, 30));
	    g.drawString ("EXIT", appletsize_x - 80, 30);

	    // This draws the image of the white large pawn and the black large pawn.
	    g.drawImage (img8, 100, 100, null);
	    g.drawImage (img9, appletsize_x - 400, 100, null);

	    // This draws how many wins white and black have in the chess game, respectively.
	    g.setFont (new Font ("Algerian", 0, 90));
	    g.drawString (Integer.toString (numberOfWins [0]), 205, 170 + img8.getHeight (null));
	    g.drawString (Integer.toString (numberOfWins [1]), appletsize_x - 275, 170 + img8.getHeight (null));

	    // This draws the buttons for play game, reset, and main menu, respectively.
	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (gameScreenHover == true)
	    {
		// If no moves have been played, then it displays "play game". Otherwise, it displays "continue".
		if (numberOfMoves == 0)
		    g.drawImage (buttonLight5, 100, 650, null);
		// If no moves have been played, then it displays "continue". Otherwise, it displays "continue".
		else
		    g.drawImage (buttonLight7, 100, 650, null);
	    }
	    else
	    {
		// If no moves have been played, then it displays "play game". Otherwise, it displays "continue".
		if (numberOfMoves == 0)
		    g.drawImage (button5, 100, 650, null);
		// If no moves have been played, then it displays "continue". Otherwise, it displays "continue".
		else
		    g.drawImage (button7, 100, 650, null);
	    }

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (resetHover == true)
		g.drawImage (buttonLight6, 340, 580, null);
	    else
		g.drawImage (button6, 340, 580, null);

	    // Draws the opaque picture or the transparent picture based on if the mouse hovers over the button.
	    if (mainMenuHover == true)
		g.drawImage (buttonLight1, appletsize_x - 400, 650, null);
	    else
		g.drawImage (button1, appletsize_x - 400, 650, null);
	}
    } // paint method


    /*****

    LOGIC SECTION

    The following section of code includes all the logic behind the game of chess, such as:
    - Moving pieces on the board.
    - Deciding how pieces on the board are allowed to move.
    - Which situations result in special cases such as checkmate, stalemate, or a draw.

    *****/


    /*
	This method is the main method in deciding how pieces can mve in chess. It is linked from the mouseClicked method, which refers to this to check all the possible moves.
	There are no parameters and nothing is returned because all the variables that are altered in the method are static variables, which means that they can be accessed anywhere in the class.
    */
    public void movePiece ()
    {
	// If the mouse has not been clicked, it goes through the following code.
	if (mouseWasClicked == -1 || mouseWasClicked == 0)
	{
	    returnPos (mx, my); // Returns the position in terms of the chess board of the piece.
	    // System.out.println ("Location: " + piecePosOriginal [1] + ", " + piecePosOriginal [0]); // Use is temporary. Displays the location of the square clicked on.

	    // If there is a piece on the square that is clicked on, or if the value in the 2D array is NOT 0 (no piece), then a move may be made with that piece.
	    if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] != 0)
	    {
		// If statement checks if it is white's turn, and if the piece picked was a white piece.
		if (numberOfMoves % 2 == 0 && chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] < 10)
		{
		    checkWhiteValidMoves (); // The checkWhiteValidMoves method is another method for that specific purpose.
		    pieceTemp = chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]]; // A temporary variable is created to store what type of piece ws clicked on.
		    mouseWasClicked = 1; // The mouseWasClicked variable changes to 1 to show that a square has been clicked on.
		}
		// If statement checks if it is black's turn, and if the piece picked was a black piece.
		else if (numberOfMoves % 2 == 1 && chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] > 10)
		{
		    checkBlackValidMoves (); // The checkBlackValidMoves method is another method for that specific purpose.
		    pieceTemp = chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]]; // A temporary variable is created to store what type of piece ws clicked on.
		    mouseWasClicked = 1; // The mouseWasClicked variable changes to 1 to show that a square has been clicked on.
		}
	    }
	}
	// If the mouse has been clicked, then it goes through the following code.
	else if (mouseWasClicked == 1)
	{
	    invalidMoveClicks = 0;

	    returnPos (nx, ny); // Returns the position in terms of the chess board of the piece.
	    // System.out.println ("Location: " + piecePosNew [1] + ", " + piecePosNew [0]); // Use is temporary. Displays the location of the square clicked on.

	    // This if statement checks if the new position is the same as the original position.
	    if (piecePosOriginal [0] != piecePosNew [0] || piecePosOriginal [1] != piecePosNew [1])
	    {
		// This for loop goes through all of the available moves for the piece clicked on.
		for (int i = 0 ; i < counterOfMoves ; i++)
		{
		    // This if statement checks if the new piece position is equal to one of the positions in the available moves list. Then, the position is passed.
		    if (piecePosNew [0] == availableMoves [i] [0] && piecePosNew [1] == availableMoves [i] [1])
		    {
			// If the new position is the opposite side for the white pawn, then the pawn should be promoted into a white rook.
			if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 1 && piecePosNew [0] == 0)
			{
			    chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] = 0; // The square that was clicked on originally is then emptied.
			    chessBoardArray [piecePosNew [0]] [piecePosNew [1]] = 2; // The new square that has been clicked on is assigned to be a white rook, as the pawn has promoted.
			    mouseWasClicked = 0; // The mouseWasClicked variable changes to 0 to show that the move has been completed.
			    numberOfMoves++; // The number of moves is incremented by 1.
			}
			// If the new position is the opposite side for the black pawn, then the pawn should be promoted into a black rook.
			else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 11 && piecePosNew [0] == 7)
			{
			    chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] = 0; // The square that was clicked on originally is then emptied.
			    chessBoardArray [piecePosNew [0]] [piecePosNew [1]] = 12; // The new square that has been clicked on is assigned to be a black rook, as the pawn has promoted.
			    mouseWasClicked = 0; // The mouseWasClicked variable changes to 0 to show that the move has been completed.
			    numberOfMoves++; // The number of moves is incremented by 1.
			}
			// Otherwise, under normal conditions, simply move the piece to the new square.
			else
			{
			    chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] = 0; // The square that was clicked on originally is then emptied.
			    chessBoardArray [piecePosNew [0]] [piecePosNew [1]] = pieceTemp; // The new square that has been clicked on is assigned the piece of the old square.
			    mouseWasClicked = 0; // The mouseWasClicked variable changes to 0 to show that the move has been completed.
			    numberOfMoves++; // The number of moves is incremented by 1.
			}
		    }
		    // The number of invalid moves checked increases by 1 each time.
		    else
		    {
			invalidMoveClicks++;
		    }
		}
	    }

	    // If the same position is clicked on or if a invalid position is clicked on, then a move has not occurred.
	    // Also, if the number of invalid moves equals to the counter of moves, or if the move clicked does not match ANY of the possible moves, then the square is unhighlighted.
	    if ((piecePosOriginal [0] == piecePosNew [0] && piecePosOriginal [1] == piecePosNew [1]) || invalidMoveClicks == counterOfMoves)
	    {
		mouseWasClicked = 0; // The mouseWasClicked variable changes to 0 to show that the move has not been completed.
	    }

	    // This if statement checks if white has just moved (odd) or if black has just moved (even). Then, it can be determined what input is to be put in.
	    if (numberOfMoves % 2 == 1)
	    {
		checkForWin (1);
		// Checks if white has won.
		if (whiteHasWon == true)
		{
		    numberOfWins [0]++;
		}
	    }
	    else if (numberOfMoves % 2 == 0)
	    {
		checkForWin (2);
		// Checks if black has won.
		if (blackHasWon == true)
		{
		    numberOfWins [1]++;
		}
	    }
	}
    }


    /*
	This method checks the moves that are valid for the white piece that has been clicked on.
    */
    public void checkWhiteValidMoves ()
    {
	// This for loop repeats each time a piece is selected, and clears the availiableMoves 2D array with -1's to indicate that no move is available.
	for (int i = 0 ; i < availableMoves.length ; i++)
	{
	    for (int j = 0 ; j < availableMoves [i].length ; j++)
	    {
		availableMoves [i] [j] = -1;
	    }
	}

	// The counter, which stores how many valid moves are actually in the 2D array availableMoves.
	counterOfMoves = 0;

	// This if loop checks which piece occupies the square that was clicked on.
	// If the square has a value of 1 in the array, it must be a white pawn.
	if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 1)
	{
	    // Then, the available moves for the white pawn are checked.
	    whitePawnValidMoves ();
	}

	// If the square has a value of 2 in the array, it must be a white rook.
	else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 2)
	{
	    // Then, the available moves for the white rook are checked.
	    whiteRookValidMoves ();
	}

	// If the square has a value of 3 in the array, it must be a white king.
	else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 3)
	{
	    // Then, the available moves for the white king are checked.
	    whiteKingValidMoves ();
	}
    }


    /*
	This method checks the moves that are valid for the black piece that has been clicked on.
    */
    public void checkBlackValidMoves ()
    {
	// This for loop repeats each time a piece is selected, and clears the availiableMoves 2D array with -1's to indicate that no move is available.
	for (int i = 0 ; i < availableMoves.length ; i++)
	{
	    for (int j = 0 ; j < availableMoves [i].length ; j++)
	    {
		availableMoves [i] [j] = -1;
	    }
	}

	// The counter, which stores how many valid moves are actually in the 2D array availableMoves.
	counterOfMoves = 0;

	// This if loop checks which piece occupies the square that was clicked on.
	// If the square has a value of 11 in the array, it must be a black pawn.
	if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 11)
	{
	    // Then, the available moves for the black pawn are checked.
	    blackPawnValidMoves ();
	}
	// If the square has a value of 12 in the array, it must be a black rook.
	else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 12)
	{
	    // Then, the available moves for the black rook are checked.
	    blackRookValidMoves ();
	}
	// If the square has a value of 13 in the array, it must be a black king.
	else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1]] == 13)
	{
	    // Then, the available moves for the black king are checked.
	    blackKingValidMoves ();
	}
    }


    /*
	This method checks the moves that are valid for the white pawn that was clicked.
    */
    public void whitePawnValidMoves ()
    {
	// This if statement says that if the there is no piece 1 square in front of the white pawn, then the pawn can be moved to that square.
	if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1]] == 0)
	{
	    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
	    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
	    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
	    counterOfMoves++;
	}

	// This if statement checks if there is a white pawn on the 7th row, or in its original position on the board.
	if (piecePosOriginal [0] == 6)
	{
	    // If the two squares in front of the white pawn are open, then this pawn can also move 2 squares forward.
	    if (chessBoardArray [piecePosOriginal [0] - 2] [piecePosOriginal [1]] == 0 && chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1]] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 2;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	}

	// This checks whether the white pawn is past the first column or not.
	if (piecePosOriginal [1] > 0)
	{
	    // If there is a piece in a position that is diagonally in the top left corner, and it is of the opponent (black), then that piece can be captured.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] - 1] > 10)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// This checks whether the white pawn is past the eigth column or not.
	if (piecePosOriginal [1] < 7)
	{
	    // If there is a piece in a position that is diagonally in the top right corner, and it is of the opponent (black), then that piece can be captured.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] + 1] > 10)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}
    }


    /*
	This method checks the moves that are valid for the black pawn that was clicked.
    */
    public void blackPawnValidMoves ()
    {
	// This if statement says that if the there is no piece 1 square in front of the black pawn, then the pawn can be moved to that square.
	if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1]] == 0)
	{
	    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
	    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
	    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
	    counterOfMoves++;
	}

	// This if statement checks if there is a black pawn on the 2nd row, or in its original position on the board.
	if (piecePosOriginal [0] == 1)
	{
	    // If the two squares in front of the black pawn are open, then this pawn can also move 2 squares forward.
	    if (chessBoardArray [piecePosOriginal [0] + 2] [piecePosOriginal [1]] == 0 && chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1]] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 2;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	}

	// This checks whether the black pawn is past the first column or not.
	if (piecePosOriginal [1] > 0)
	{
	    // If there is a piece in a position that is diagonally in the bottom left corner, and it is of the opponent (white), then that piece can be captured.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] - 1] > 0 && chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] - 1] < 10)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// This checks whether the black pawn is past the first column or not.
	if (piecePosOriginal [1] < 7)
	{
	    // If there is a piece in a position that is diagonally in the bottom right corner, and it is of the opponent (white), then that piece can be captured.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] + 1] > 0 && chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] + 1] < 10)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}
    }


    /*
	This method checks the moves that are valid for the white rook that was clicked.
    */
    public void whiteRookValidMoves ()
    {
	// Sets the variables for the do while condition loop.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the UP direction.
	do
	{
	    // The condition is only passed if the square in front of the rook has a valid index on the chess board.
	    if (piecePosOriginal [0] - counter > 0)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly up if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0] - counter] [piecePosOriginal [1]] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// If there is a black piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0] - counter] [piecePosOriginal [1]] > 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);

	// Allows the same spot to be defined as a valid move.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the DOWN direction.
	do
	{
	    // The condition is only passed if the square below the rook has a valid index on the chess board.
	    if (piecePosOriginal [0] + counter < 7)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly down if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0] + counter] [piecePosOriginal [1]] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// If there is a black piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0] + counter] [piecePosOriginal [1]] > 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);

	// Sets the variables for the do while condition loop.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the RIGHT direction.
	do
	{
	    // The condition is only passed if the square to the right of the rook has a valid index on the chess board.
	    if (piecePosOriginal [1] + counter < 7)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly right if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + counter] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + counter;
		    counterOfMoves++;
		}
		// If there is a black piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + counter] > 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + counter;
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);

	// Sets the variables for the do while condition loop.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the LEFT direction.
	do
	{
	    // The condition is only passed if the square to the left of the rook has a valid index on the chess board.
	    if (piecePosOriginal [1] - counter > 0)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly left if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - counter] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - counter;
		    counterOfMoves++;
		}
		// If there is a black piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - counter] > 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - counter;
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);
    }


    /*
	This method checks the moves that are valid for the white rook that was clicked.
    */
    public void blackRookValidMoves ()
    {
	// Sets the variables for the do while condition loop.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the UP direction.
	do
	{
	    // The condition is only passed if the square in front of the rook has a valid index on the chess board.
	    if (piecePosOriginal [0] - counter > 0)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly up if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0] - counter] [piecePosOriginal [1]] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// If there is a white piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0] - counter] [piecePosOriginal [1]] < 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);

	// Allows the same spot to be defined as a valid move.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the DOWN direction.
	do
	{
	    // The condition is only passed if the square below the rook has a valid index on the chess board.
	    if (piecePosOriginal [0] + counter < 7)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly down if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0] + counter] [piecePosOriginal [1]] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// If there is a white piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0] + counter] [piecePosOriginal [1]] < 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + counter;
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);

	// Sets the variables for the do while condition loop.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the RIGHT direction.
	do
	{
	    // The condition is only passed if the square to the right of the rook has a valid index on the chess board.
	    if (piecePosOriginal [1] + counter < 7)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly right if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + counter] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + counter;
		    counterOfMoves++;
		}
		// If there is a white piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + counter] < 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + counter;
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);

	// Sets the variables for the do while condition loop.
	rookCanMove = true;
	counter = 0;

	// This do while loop goes through the possible situations where the rook can move in the LEFT direction.
	do
	{
	    // The condition is only passed if the square to the left of the rook has a valid index on the chess board.
	    if (piecePosOriginal [1] - counter > 0)
	    {
		// The counter is now increased.
		counter++;
		// The rook can move directly left if there are blank squares available.
		if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - counter] == 0)
		{
		    rookCanMove = true;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - counter;
		    counterOfMoves++;
		}
		// If there is a white piece on the board, then it can take the piece, but cannot move further past it, which is why the rookCanMove condition is set to false.
		else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - counter] < 10)
		{
		    rookCanMove = false;

		    // This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		    availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		    availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - counter;
		    counterOfMoves++;
		}
		// In all other cases, there are no valid moves forward for the rook, which means that the do while loop is ended.
		else
		{
		    rookCanMove = false;
		}
	    }
	    // In the case that the first condition is not satisfied, the boolean is also set to false.
	    else
	    {
		rookCanMove = false;
	    }
	}
	// This condition loops while the boolean rookCanMove is true.
	while (rookCanMove == true);
    }


    /*
	This method checks the moves that are valid for the white king that was clicked.
    */
    public void whiteKingValidMoves ()
    {
	// The condition is only passed if the square above the king has a valid index on the chess board.
	if (piecePosOriginal [0] - 1 >= 0)
	{
	    // Checks if the piece above has no piece.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1]] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1]] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the top-right of the king has a valid index on the chess board.
	if (piecePosOriginal [0] - 1 >= 0 && piecePosOriginal [1] + 1 <= 7)
	{
	    // Checks if the piece to the top-right has no piece.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] + 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] + 1] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square below the king has a valid index on the chess board.
	if (piecePosOriginal [0] + 1 <= 7)
	{
	    // Checks if the piece below has no piece.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1]] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1]] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the top-left the king has a valid index on the chess board.
	if (piecePosOriginal [0] - 1 >= 0 && piecePosOriginal [1] - 1 >= 0)
	{
	    // Checks if the piece to the top-left has no piece.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] - 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] - 1] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the right the king has a valid index on the chess board.
	if (piecePosOriginal [1] + 1 <= 7)
	{
	    // Checks if the piece to the right has no piece.
	    if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + 1] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the bottom-left the king has a valid index on the chess board.
	if (piecePosOriginal [0] + 1 <= 7 && piecePosOriginal [1] - 1 >= 0)
	{
	    // Checks if the piece to the bottom-left has no piece.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] - 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] - 1] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the left the king has a valid index on the chess board.
	if (piecePosOriginal [1] - 1 >= 0)
	{
	    // Checks if the piece to the left has no piece.
	    if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - 1] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the bottom-rigth the king has a valid index on the chess board.
	if (piecePosOriginal [0] + 1 <= 7 && piecePosOriginal [1] + 1 <= 7)
	{
	    // Checks if the piece to the bottom-right has no piece.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] + 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] + 1] > 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}
    }


    /*
	This method checks the moves that are valid for the black king that was clicked.
    */
    public void blackKingValidMoves ()
    {
	// The condition is only passed if the square above the king has a valid index on the chess board.
	if (piecePosOriginal [0] - 1 >= 0)
	{
	    // Checks if the piece above has no piece.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1]] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1]] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the top-right of the king has a valid index on the chess board.
	if (piecePosOriginal [0] - 1 >= 0 && piecePosOriginal [1] + 1 <= 7)
	{
	    // Checks if the piece to the top-right has no piece.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] + 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] + 1] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square below the king has a valid index on the chess board.
	if (piecePosOriginal [0] + 1 <= 7)
	{
	    // Checks if the piece below has no piece.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1]] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1]] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1];
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the top-left the king has a valid index on the chess board.
	if (piecePosOriginal [0] - 1 >= 0 && piecePosOriginal [1] - 1 >= 0)
	{
	    // Checks if the piece to the top-left has no piece.
	    if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] - 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] - 1] [piecePosOriginal [1] - 1] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] - 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the right the king has a valid index on the chess board.
	if (piecePosOriginal [1] + 1 <= 7)
	{
	    // Checks if the piece to the right has no piece.
	    if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] + 1] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the bottom-left the king has a valid index on the chess board.
	if (piecePosOriginal [0] + 1 <= 7 && piecePosOriginal [1] - 1 >= 0)
	{
	    // Checks if the piece to the bottom-left has no piece.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] - 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] - 1] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the left the king has a valid index on the chess board.
	if (piecePosOriginal [1] - 1 >= 0)
	{
	    // Checks if the piece to the left has no piece.
	    if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0]] [piecePosOriginal [1] - 1] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0];
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] - 1;
		counterOfMoves++;
	    }
	}

	// The condition is only passed if the square to the bottom-rigth the king has a valid index on the chess board.
	if (piecePosOriginal [0] + 1 <= 7 && piecePosOriginal [1] + 1 <= 7)
	{
	    // Checks if the piece to the bottom-right has no piece.
	    if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] + 1] == 0)
	    {
		// This stores that move in the availableMoves array, and the referenced squares will be highlighted on the screen.
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	    // If that square has a piece, it can be taken, so long as it does not put the king in check.
	    else if (chessBoardArray [piecePosOriginal [0] + 1] [piecePosOriginal [1] + 1] < 10)
	    {
		availableMoves [counterOfMoves] [0] = piecePosOriginal [0] + 1;
		availableMoves [counterOfMoves] [1] = piecePosOriginal [1] + 1;
		counterOfMoves++;
	    }
	}
    }


    /*
	This method checks if the move made wins the game. Essentially, if the king of the opposite colour is captured, then the colour that captured the opposite coloured king wins.
    */
    public void checkForWin (int colour)
    {
	// If the colour checked for is 1, or if it is checking for a win for white.
	if (colour == 1)
	{
	    // Sets the boolean equal to true beforehand.
	    whiteHasWon = true;

	    // If an instance of a black king is found on the board, then white has not won yet.
	    for (int i = 0 ; i < 8 ; i++)
	    {
		for (int j = 0 ; j < 8 ; j++)
		{
		    if (chessBoardArray [i] [j] == 13)
		    {
			whiteHasWon = false;
		    }
		}
	    }
	}
	// If the colour checked for is 2, or if it is checking for a win for black.
	else if (colour == 2)
	{
	    // Sets the boolean equal to true beforehand.
	    blackHasWon = true;

	    // If an instance of a white king is found on the board, then black has not won yet.
	    for (int i = 0 ; i < 8 ; i++)
	    {
		for (int j = 0 ; j < 8 ; j++)
		{
		    if (chessBoardArray [i] [j] == 3)
		    {
			blackHasWon = false;
		    }
		}
	    }
	}
    }


    /*
	This method returns the position of a click from the mouse as a location on the 2D array.
    */
    public void returnPos (int xClicked, int yClicked)
    {
	// If the mouse has just been clicked, then the piece location is stored in the array piecePosOriginal.
	if (mouseWasClicked == -1 || mouseWasClicked == 0)
	{
	    // Refers to the first row.
	    if (xClicked >= x_pos && xClicked < x_pos + size)
	    {
		piecePosOriginal [1] = 0;
	    }

	    // Refers to the second row.
	    else if (xClicked >= x_pos + size && xClicked < x_pos + 2 * size)
	    {
		piecePosOriginal [1] = 1;
	    }

	    // Refers to the third row.
	    else if (xClicked >= x_pos + 2 * size && xClicked < x_pos + 3 * size)
	    {
		piecePosOriginal [1] = 2;
	    }

	    // Refers to the fourth row.
	    else if (xClicked >= x_pos + 3 * size && xClicked < x_pos + 4 * size)
	    {
		piecePosOriginal [1] = 3;
	    }

	    // Refers to the fifth row.
	    else if (xClicked >= x_pos + 4 * size && xClicked < x_pos + 5 * size)
	    {
		piecePosOriginal [1] = 4;
	    }

	    // Refers to the sixth row.
	    else if (xClicked >= x_pos + 5 * size && xClicked < x_pos + 6 * size)
	    {
		piecePosOriginal [1] = 5;
	    }

	    // Refers to the seventh row.
	    else if (xClicked >= x_pos + 6 * size && xClicked < x_pos + 7 * size)
	    {
		piecePosOriginal [1] = 6;
	    }

	    // Refers to the eigth row.
	    else if (xClicked >= x_pos + 7 * size && xClicked <= x_pos + 8 * size)
	    {
		piecePosOriginal [1] = 7;
	    }

	    // Refers to the first column.
	    if (yClicked >= y_pos && yClicked < y_pos + size)
	    {
		piecePosOriginal [0] = 0;
	    }

	    // Refers to the second column.
	    else if (yClicked >= y_pos + size && yClicked < y_pos + 2 * size)
	    {
		piecePosOriginal [0] = 1;
	    }

	    // Refers to the third column.
	    else if (yClicked >= y_pos + 2 * size && yClicked < y_pos + 3 * size)
	    {
		piecePosOriginal [0] = 2;
	    }

	    // Refers to the fourth column.
	    else if (yClicked >= y_pos + 3 * size && yClicked < y_pos + 4 * size)
	    {
		piecePosOriginal [0] = 3;
	    }

	    // Refers to the fifth column.
	    else if (yClicked >= y_pos + 4 * size && yClicked < y_pos + 5 * size)
	    {
		piecePosOriginal [0] = 4;
	    }

	    // Refers to the sixth column.
	    else if (yClicked >= y_pos + 5 * size && yClicked < y_pos + 6 * size)
	    {
		piecePosOriginal [0] = 5;
	    }

	    // Refers to the seventh column.
	    else if (yClicked >= y_pos + 6 * size && yClicked < y_pos + 7 * size)
	    {
		piecePosOriginal [0] = 6;
	    }

	    // Refers to the eigth column.
	    else if (yClicked >= y_pos + 7 * size && yClicked <= y_pos + 8 * size)
	    {
		piecePosOriginal [0] = 7;
	    }
	}

	// If the mouse has just been clicked, then the piece location is stored in the array piecePosNew.
	else if (mouseWasClicked == 1)
	{
	    // Refers to the first row.
	    if (xClicked >= x_pos && xClicked < x_pos + size)
	    {
		piecePosNew [1] = 0;
	    }

	    // Refers to the second row.
	    else if (xClicked >= x_pos + size && xClicked < x_pos + 2 * size)
	    {
		piecePosNew [1] = 1;
	    }

	    // Refers to the third row.
	    else if (xClicked >= x_pos + 2 * size && xClicked < x_pos + 3 * size)
	    {
		piecePosNew [1] = 2;
	    }

	    // Refers to the fourth row.
	    else if (xClicked >= x_pos + 3 * size && xClicked < x_pos + 4 * size)
	    {
		piecePosNew [1] = 3;
	    }

	    // Refers to the fifth row.
	    else if (xClicked >= x_pos + 4 * size && xClicked < x_pos + 5 * size)
	    {
		piecePosNew [1] = 4;
	    }

	    // Refers to the sixth row.
	    else if (xClicked >= x_pos + 5 * size && xClicked < x_pos + 6 * size)
	    {
		piecePosNew [1] = 5;
	    }

	    // Refers to the seventh row.
	    else if (xClicked >= x_pos + 6 * size && xClicked < x_pos + 7 * size)
	    {
		piecePosNew [1] = 6;
	    }

	    // Refers to the eigth row.
	    else if (xClicked >= x_pos + 7 * size && xClicked <= x_pos + 8 * size)
	    {
		piecePosNew [1] = 7;
	    }

	    // Refers to the first column.
	    if (yClicked >= y_pos && yClicked < y_pos + size)
	    {
		piecePosNew [0] = 0;
	    }

	    // Refers to the second column.
	    else if (yClicked >= y_pos + size && yClicked < y_pos + 2 * size)
	    {
		piecePosNew [0] = 1;
	    }

	    // Refers to the third column.
	    else if (yClicked >= y_pos + 2 * size && yClicked < y_pos + 3 * size)
	    {
		piecePosNew [0] = 2;
	    }

	    // Refers to the fourth column.
	    else if (yClicked >= y_pos + 3 * size && yClicked < y_pos + 4 * size)
	    {
		piecePosNew [0] = 3;
	    }

	    // Refers to the fifth column.
	    else if (yClicked >= y_pos + 4 * size && yClicked < y_pos + 5 * size)
	    {
		piecePosNew [0] = 4;
	    }

	    // Refers to the sixth column.
	    else if (yClicked >= y_pos + 5 * size && yClicked < y_pos + 6 * size)
	    {
		piecePosNew [0] = 5;
	    }

	    // Refers to the seventh column.
	    else if (yClicked >= y_pos + 6 * size && yClicked < y_pos + 7 * size)
	    {
		piecePosNew [0] = 6;
	    }

	    // Refers to the eigth column.
	    else if (yClicked >= y_pos + 7 * size && yClicked <= y_pos + 8 * size)
	    {
		piecePosNew [0] = 7;
	    }
	}
    }
} // ChessBasic class
