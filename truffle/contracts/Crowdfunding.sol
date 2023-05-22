//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Crowdfunding is Ownable {
    address public admin;
    address public tokenAddress;

    IERC20 public token;
    string public title;
    uint public noOfContributors;
    uint public deadline;
    uint public goal;
    uint public raisedAmount;
    uint public totalRequestAmount;

    struct Request {
        string title;
        address recipient;
        uint amount;
        bool completed;
        uint noOfVoters;
        uint valueOfVotes;
        mapping(address => bool) voters;
    }

    event ContributeEvent(
        // address _projectAddress,
        address _contributor,
        address _tokenAddress,
        uint _amount
    );
    event GetRefundEvent(
        // address _projectAddress,
        address _contributor,
        address _RefundToken,
        uint _RefundAmount
    );
    event CreateRequestEvent(
        uint requestNum,
        // address _projectAddress,
        address _projectCreator,
        string _title,
        address _recipient,
        uint _amount
    );
    event ReceiveContributionEvent(
        // address _projectAddress,
        address _projectCreator,
        address _recipient,
        uint _amount,
        uint _requestNo
    );
    event voteRequestEvent(
        // address _projectAddress,
        address _voter,
        uint _requestNo,
        uint _valueOfVote
    );

    // cannot store Request in array as it contains mapping
    // therefore use mapping
    uint public numRequests;
    mapping(uint => Request) public requests;
    mapping(address => uint) public contributors;

    // // allows factory to call the functions here
    // modifier sharedOwner() {
    //     require(
    //         msg.sender == owner() || msg.sender == crowdfundingFactory,
    //         "You are neither the owner nor the factory"
    //     );
    //     _;
    // }

    constructor(
        uint _goal,
        uint _deadline,
        address _acceptingThisToken,
        string memory _title
    ) {
        require(_deadline > block.timestamp, "Deadline must be in the future");
        require(isToken(_acceptingThisToken), "Address provided is not token");
        require(
            isContract(_acceptingThisToken),
            "Address provided is not a contract"
        );

        title = _title;
        goal = _goal;
        deadline = _deadline;
        admin = msg.sender;

        token = IERC20(_acceptingThisToken);
        tokenAddress = _acceptingThisToken;
    }

    function contribute(uint _amount) public {
        require(
            token.allowance(msg.sender, address(this)) >= _amount,
            "token spend not approved"
        );

        if (contributors[msg.sender] == 0) {
            unchecked {
                noOfContributors++;
            }
        }
        token.transferFrom(msg.sender, address(this), _amount);
        unchecked {
            contributors[msg.sender] += _amount;
            raisedAmount += _amount;
        }

        emit ContributeEvent(msg.sender, tokenAddress, _amount);
    }

    function getRefund() public returns (uint) {
        require(block.timestamp >= deadline, "Deadline has not passed");
        require(raisedAmount < goal, "Raised amount is more than goal");
        require(contributors[msg.sender] > 0, "You need to be a contributor");

        uint _amount = contributors[msg.sender];
        contributors[msg.sender] = 0;
        token.approve(msg.sender, _amount);
        token.transfer(msg.sender, _amount);
        emit GetRefundEvent(msg.sender, address(token), _amount);
        return _amount;
    }

    function createRequest(
        string memory _title,
        address _recipient,
        uint _amount
    ) public onlyOwner {
        if (raisedAmount > 0) {
            require(
                (totalRequestAmount + _amount) <= raisedAmount,
                "This transaction will cause totalRequestAmount to exceed raisedAmount"
            );
        }

        uint _requestNo = numRequests;
        Request storage newRequest = requests[numRequests];
        numRequests++;

        newRequest.title = _title;
        newRequest.recipient = _recipient;
        newRequest.amount = _amount;
        newRequest.completed = false;
        newRequest.noOfVoters = 0;

        totalRequestAmount += _amount;

        emit CreateRequestEvent(
            _requestNo,
            msg.sender,
            _title,
            _recipient,
            _amount
        );
    }

    function voteRequest(uint _requestNo) public {
        require(numRequests >= _requestNo, "Request does not exist.");
        require(
            msg.sender != owner(),
            "As the owner, not allowed to vote for this request"
        );

        // must have contributed before voting
        require(
            contributors[msg.sender] > 0,
            "You must be a contributor to vote!"
        );

        Request storage thisRequest = requests[_requestNo];
        require(
            thisRequest.voters[msg.sender] == false,
            "You have already voted"
        );
        require(
            thisRequest.recipient != address(0),
            "Request has not been initiated"
        );

        uint _valueOfVote = contributors[msg.sender];
        thisRequest.valueOfVotes += _valueOfVote;
        thisRequest.noOfVoters++;
        thisRequest.voters[msg.sender] = true;
        emit voteRequestEvent(msg.sender, _requestNo, _valueOfVote);
    }

    function receiveContribution(
        uint _requestNo
    ) public onlyOwner returns (address, uint) {
        require(
            raisedAmount >= goal,
            "raisedAmount must be more than or equal to goal"
        );

        Request storage thisRequest = requests[_requestNo];
        require(
            thisRequest.completed == false,
            "The request has been completed."
        );
        uint ratio = goal / thisRequest.valueOfVotes;
        require(ratio < 2, "value of votes fail to meet requirement");

        address _recipient = thisRequest.recipient;
        uint _amount = thisRequest.amount;
        token.transfer(_recipient, _amount);
        thisRequest.completed = true;
        emit ReceiveContributionEvent(
            msg.sender,
            _recipient,
            _amount,
            _requestNo
        );
        return (thisRequest.recipient, thisRequest.amount);
    }

    function getRequestTitle(
        uint _requestNo
    ) external view returns (string memory) {
        return requests[_requestNo].title;
    }

    function getRequestRecipient(
        uint _requestNo
    ) external view returns (address) {
        return requests[_requestNo].recipient;
    }

    function getRequestAmount(uint _requestNo) external view returns (uint) {
        return requests[_requestNo].amount;
    }

    function getRequestCompleted(uint _requestNo) external view returns (bool) {
        return requests[_requestNo].completed;
    }

    function getRequestNumOfVoters(
        uint _requestNo
    ) external view returns (uint) {
        return requests[_requestNo].noOfVoters;
    }

    function getRequestValueOfVotes(
        uint _requestNo
    ) external view returns (uint) {
        return requests[_requestNo].valueOfVotes;
    }

    function currBlockTimestamp() public view returns (uint) {
        return block.timestamp;
    }

    // this function will run at constructor
    function isContract(address _address) public view returns (bool) {
        uint size;
        assembly {
            size := extcodesize(_address)
        }
        return size > 0;
    }

    // this function will run at constructor
    function isToken(address _address) public view returns (bool) {
        ERC20 testAddress = ERC20(_address);
        require(testAddress.balanceOf(msg.sender) >= 0, "method doesn't exist");
        require(testAddress.totalSupply() > 0, "Supply is 0");
        bytes memory _testString = bytes(testAddress.name());

        return _testString.length > 0;
    }
}
