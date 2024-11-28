module Common {
    requires static lombok;
    exports common.command;
    exports common.command.admin;
    exports common.command.client;
    exports common.command.supervisor;
    exports common.model;
}
