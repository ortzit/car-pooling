package org.carPooling.shared.application

interface CommandUseCase<Command> {
    fun run(command: Command)
}