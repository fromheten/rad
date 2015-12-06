# Mode

## What is a mode?
Rad is a modal editor. A mode is the definition of what happens when you give an input.

In effect, a mode is nothing more than a funciton, that takes a keypress, and does something based on that input.

## Rads modes

Rad comes with the following modes:

* Insert mode
This is the default mode, and the one you will spend most of your time in. It behaves like any other text editor - the text you type shows up in your buffer.

* Command mode
Rads command mode is similar to the normal mode you find in vim. You run commands, by combining different keys.

The path to different commands is defined in `rad.mode/key-map`.

## Extending Rads command mode
This is currently not possible. When loading third-party packages is implemented, they will be able to provide a key-map that will be active when that package is active.

### Command mode timeout
In Command Mode, one second of inactivity will put you back into normal mode.
