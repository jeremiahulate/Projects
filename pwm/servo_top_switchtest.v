module servo_top_switchtest(
    input clk,            // Basys3 clock, e.g., 100MHz
    input [1:0] sw_claw,  // Two onboard switches to control the claw state
    input [1:0] sw_lift,  // (Optional) Two onboard switches for the lift state, or tie to a specific value
    output clawsignal,
    output liftsignal
);

    // Assign the switch values to the control states
    wire [1:0] clawstate = sw_claw;  
    wire [1:0] liftstate = sw_lift;   // or: assign liftstate = 2'b00; if not using switches for lift

    // Instantiate your servo_top module
    servo_top u_servo_top (
        .clk(clk),
        .clawstate(clawstate),
        .liftstate(liftstate),
        .clawsignal(clawsignal),
        .liftsignal(liftsignal)
    );

endmodule