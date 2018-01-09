# FRC-PowerUp-Simulator
Simulates games in the 2018 FRC game: Power Up
Created by Team 4206, The RoboVikes
Carson FitzGibbon

This program simulates games between red and blue alliances in PowerUp. It ignores the points for crossing the autoline because I assumed most robots would be doing that, and if you do want it simulated you can just add the earned points to the total yourself.

The program is also able to take multiple CSV files and read the commands inside, turning them into actions during the match. I have included a template for creating these CSVs and below are some rules for how to create your own commands.

There are seven commands you can use:

mySwitch - adds a cube to your side's switch

oppSwitch - adds a cube to the other side's switch (your alliance color, obviously)

scale - adds a cube to the scale

+boost - adds a cube to the boost powerup

+force - adds a cube to the force powerup

+levitate - adds a cube to the levitate powerup

+climb - queues 1 robot to climb


Activating powerups is done manually. This is because the activation of powerups is very reactionary based on what the opponent does during the game.

A robot can execute up to 6 commands per second, and the format for each line in the CSV is:

timestamp,command,command,command,command,command,command

ex: 

30,scale,mySwitch

31,oppSwitch

32

33,+boost


There are six total lines to simulate six total robots in a single game, which represents a game with 2 full alliances. However, you can also play with incomplete teams, a player with preprogrammed teams, or only one alliance against a real player. You can also simply play one player against another.

Finally, there is a time-warp textbox that allows you to multiply the speed at which time passes on the simulation.

If you want to work on the source code, be warned that I am self taught and much of my code is illogical, redundant, and messy. Feel free to report bugs as you find them or request additions to the program.
