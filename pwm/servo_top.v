//`timescale 1ns / 1ps
//`include "pwm_servo.v"
//`include "pwm_dc.v"

module servo_top(
    input clk,
    input [1:0] clawstate, //for the states of the claw 
    input [1:0] liftstate,  //for the states of lifting mechanism  
    output clawsignal,
    output liftsignal
);

    //holds duty cycles for the claw
    parameter   claw = 22'd0, 
                clawclosed = 22'd130_000,
                clawopen = 22'd30_000;
 
    //holds duty cycles for the lifting mechanism (dc motor)
    parameter   lift = 8'd0,
                liftup = 8'd200, // motor forward
                liftdown = 8'd50; // motor backward

    //wires to initiate the duty cycle
    reg [21:0] clawdc = 22'd0;
    reg [7:0] liftdc = 8'd0;
    reg[1:0] state;

    //claw PWM
    pwm_servo clawPWM (
        .clk(clk),
        .dutycycle(clawdc),
        .signal(clawsignal)
    );

    //lift PWM
    pwm_dc liftPWM (
        .clk(clk),
        .dc_dutycycle(liftdc),
        .dc_signal(liftsignal)
    );


    // assign duty cycles using states
    always @(posedge clk) begin

        //claw duty cycle where 
        //00 is nothing, 01 is open, 10 is closed
        case (clawstate)
            2'b00: clawdc <= claw; 
            2'b01: clawdc <= clawopen;  
            2'b10: clawdc <= clawclosed;
            default: clawdc <= claw;
        endcase

        // lift duty cycle where
        // 00 is nothing, 01 is up, 10 is down
        case (liftstate)
            2'b00: liftdc <= lift; 
            2'b01: liftdc <= liftup; // motor forward
            2'b10: liftdc <= liftdown; // motor backward
            default: liftdc <= lift;
        endcase
    end
endmodule